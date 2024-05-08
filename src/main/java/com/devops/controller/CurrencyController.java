package com.devops.controller;

import com.devops.DTOS.CurrencyResponseDto;
import com.devops.entity.Currency;
import com.devops.service.CurrencyService;
import com.devops.service.commend.CommendContainer;
import com.devops.service.commend.GetCurrency;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

import static com.devops.enumaration.CurrencyStrategyName.CURRENCY_WITH_OPEN_FEIGN;
import static com.devops.enumaration.CurrencyStrategyName.CURRENCY_WITH_REST_TEMPLATE;


@RestController
@RequestMapping("/currency")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    private final CommendContainer commendContainer;

    @PutMapping("/update/{userId}")
    public Mono<String> updateCurrency (@PathVariable String userId) {
        return currencyService.updateData(Long.valueOf(userId));
    }

    @GetMapping("/convert")
    public Mono<Double> currencyConvert (
            @RequestParam String fromCurrency,
            @RequestParam String toCurrency,
            @RequestParam Double amount
    ) {
        return currencyService.convertCurrency(fromCurrency, toCurrency, amount);
    }

    @GetMapping("/get-by-code")
    public Mono<CurrencyResponseDto> getCurrencyByCode(@RequestParam Integer code) {
        return currencyService.getByCode(code);
    }

    @GetMapping("/get-by-ccy")
    public Mono<CurrencyResponseDto> getCurrencyByCcy(@RequestParam String ccy) {
        return currencyService.getByCcy(ccy);
    }

    @GetMapping(value = "/get-all", produces = "application/json")
    public Flux<Currency> getAllCurrencies(@RequestParam String getCurrencyStrategy) {
        return Flux.fromIterable(commendContainer.getCurrencyService(getCurrencyStrategy).getAllCurrency());
    }

    @PutMapping(value = "/save",produces = "application/json")
    public Mono<String> saveCurrency() {
        currencyService.saveCurrency();
        return Mono.just("Currency data saved 🙌");
    }
}
