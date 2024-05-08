package com.devops.service.impl;


import com.devops.DTOS.CurrencyReadDto;
import com.devops.DTOS.CurrencyResponseDto;
import com.devops.client.CurrencyFeignClient;
import com.devops.entity.Currency;
import com.devops.entity.UserRole;
import com.devops.exceptions.DataNotFoundException;
import com.devops.repository.CurrencyRepository;
import com.devops.repository.UserRepository;
import com.devops.service.CurrencyService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyFeignClient currencyFeignClient;

    private final CurrencyRepository currencyRepository;

    private final UserRepository userRepository;

    public void saveCurrency() {
        List<Currency> list = getAllCurrency();
        currencyRepository.saveAll(list).then().subscribe();
        log.info("Currency data saved 🙌");
    }

    @Override
    public List<Currency> getAllCurrency() {
        List<CurrencyReadDto> currencies = currencyFeignClient.getAllCurrency();

        currencies.add(
                new CurrencyReadDto(
                76L,
                "0",
                "UZS",
                "O'zbek so'mi",
                1D,
                LocalDate.now()
        ));
        return currencies.stream().map((data) -> Currency.builder()
                .id(data.getBankId())
                .rate(data.getRate())
                .code(Integer.valueOf(data.getCode()))
                .ccy(data.getCcy())
                .ccyNmUz(data.getCcyNmUz())
                .date(data.getDate())
                .build()).toList();
    }

    @Override
    public Mono<String> updateData(Long userId) {
        return userRepository.findById(userId)
                .flatMap(user -> {
                    if (user.getRole().equals(UserRole.ADMIN_ROLE.name())) {
                        updateCurrencies();
                        return Mono.just("Currency database updated ✅");
                    } else {
                        return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied"));
                    }
                })
                .switchIfEmpty(Mono.error(new DataNotFoundException("User not found with id: " + userId)));
    }

    @Override
    public Mono<Double> convertCurrency(String fromCurrency, String toCurrency, Double amount) {
        Mono<Currency> sourceCurrencyMono = currencyRepository.findByCcy(fromCurrency)
                .switchIfEmpty(Mono.error(new DataNotFoundException("Source currency not found: " + fromCurrency)));

        Mono<Currency> targetCurrencyMono = currencyRepository.findByCcy(toCurrency)
                .switchIfEmpty(Mono.error(new DataNotFoundException("Target currency not found: " + toCurrency)));

        return Mono.zip(sourceCurrencyMono, targetCurrencyMono)
                .flatMap(currencies -> {
                    Currency sourceCurrency = currencies.getT1();
                    Currency targetCurrency = currencies.getT2();
                    return Mono.just(
                            amount * sourceCurrency.getRate() / targetCurrency.getRate()
                    );
                });
    }

    @Override
    public Mono<CurrencyResponseDto> getByCode(Integer code) {
        return currencyRepository.findByCode(code)
                .flatMap(currency -> Mono.just(new CurrencyResponseDto(
                        currency.getId(),
                        currency.getId(),
                        currency.getCode(),
                        currency.getCcy(),
                        currency.getCcyNmUz(),
                        currency.getRate(),
                        currency.getDate()
                ))).switchIfEmpty(Mono.error(new DataNotFoundException("Currency not found with code: " + code)));
    }

    @Override
    public Mono<CurrencyResponseDto> getByCcy(String ccy) {
        return currencyRepository.findByCcy(ccy)
                .flatMap(currency -> Mono.just(new CurrencyResponseDto(
                        currency.getId(),
                        currency.getId(),
                        currency.getCode(),
                        currency.getCcy(),
                        currency.getCcyNmUz(),
                        currency.getRate(),
                        currency.getDate()
                ))).switchIfEmpty(Mono.error(new DataNotFoundException("Currency not found with ccy: "+ccy)));
    }

    @Override
    public void updateCurrencies() {
        List<Currency> fetchedCurrencies = getAllCurrency();
        for (Currency fetchedCurrency : fetchedCurrencies) {
            currencyRepository.findByCode(fetchedCurrency.getCode()).flatMap(existingCurrency -> {
                        existingCurrency.setId(fetchedCurrency.getId());
                        existingCurrency.setRate(fetchedCurrency.getRate());
                        existingCurrency.setCode(fetchedCurrency.getCode());
                        existingCurrency.setCcy(fetchedCurrency.getCcy());
                        existingCurrency.setCcyNmUz(fetchedCurrency.getCcyNmUz());
                        existingCurrency.setDate(fetchedCurrency.getDate());
                        return currencyRepository.save(existingCurrency);
                    }).switchIfEmpty(currencyRepository.save(fetchedCurrency))
                    .then()
                    .subscribe();
        }
        log.info("Currency DB updated ✅");
    }
}
