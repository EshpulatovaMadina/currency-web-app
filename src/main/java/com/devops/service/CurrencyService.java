package com.devops.service;


import com.devops.DTOS.CurrencyReadDto;
import com.devops.DTOS.CurrencyResponseDto;
import com.devops.entity.Currency;
import com.devops.entity.UserRole;
import com.devops.exceptions.DataNotFoundException;
import com.devops.repository.CurrencyRepository;
import com.devops.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyService {
    private final CurrencyRepository currencyRepository;
    private final UserRepository userRepository;
    public List<Currency> readAll() {
        String url = "https://cbu.uz/uz/arkhiv-kursov-valyut/json/";
        ObjectMapper objectMapper = new ObjectMapper();

        List<CurrencyReadDto> currencies = null;
        try {
            currencies = objectMapper.readValue(
                    new URL(url),
                    new com.fasterxml.jackson.core.type.TypeReference<List<CurrencyReadDto>>() {}
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        currencies.add(new CurrencyReadDto(
                76L,
                "0",
                "UZS",
                "O'zbek so'mi",
                1D,
                LocalDate.now()
        ));
        return currencies.stream().map((data) -> Currency.builder()
                .bankId(data.getBankId())
                .rate(data.getRate())
                .code(Integer.valueOf(data.getCode()))
                .ccy(data.getCcy())
                .ccyNmUz(data.getCcyNmUz())
                .date(data.getDate())
                .build()).toList();
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void updateCurrencyScheduled() {
        List<Currency> fetchedCurrencies = readAll();
        for (Currency fetchedCurrency : fetchedCurrencies) {
            currencyRepository.findByCode(fetchedCurrency.getCode()).flatMap(existingCurrency -> {
                        existingCurrency.setBankId(fetchedCurrency.getBankId());
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

    @PostConstruct
    public void saveCurrency() {
        List<Currency> list = readAll();
        currencyRepository.saveAll(list).then().subscribe();
        log.info("Currency data saved 🙌");
    }

    public Mono<String> updateData(Long userId) {
        return userRepository.findById(userId)
                .flatMap(user -> {
                    if (user.getRole().equals(UserRole.ADMIN_ROLE.name())) {
                        updateCurrencyScheduled();
                        return Mono.just("Currency database updated ✅");
                    } else {
                        return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied"));
                    }
                })
                .switchIfEmpty(Mono.error(new DataNotFoundException("User not found with id: " + userId)));
    }

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

    public Mono<CurrencyResponseDto> getByCode(Integer code) {
        return currencyRepository.findByCode(code)
                .flatMap(currency -> Mono.just(new CurrencyResponseDto(
                        currency.getId(),
                        currency.getBankId(),
                        currency.getCode(),
                        currency.getCcy(),
                        currency.getCcyNmUz(),
                        currency.getRate(),
                        currency.getDate()
                ))).switchIfEmpty(Mono.error(new DataNotFoundException("Currency not found with code: "+code)));
    }

    public Mono<CurrencyResponseDto> getByCcy(String ccy) {
        return currencyRepository.findByCcy(ccy)
                .flatMap(currency -> Mono.just(new CurrencyResponseDto(
                        currency.getId(),
                        currency.getBankId(),
                        currency.getCode(),
                        currency.getCcy(),
                        currency.getCcyNmUz(),
                        currency.getRate(),
                        currency.getDate()
                ))).switchIfEmpty(Mono.error(new DataNotFoundException("Currency not found with code: "+ccy)));
    }
}
