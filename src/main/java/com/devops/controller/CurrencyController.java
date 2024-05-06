package com.devops.controller;

import com.devops.DTOS.CurrencyResponseDto;
import com.devops.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;


@RestController
@RequestMapping("/currency")
@RequiredArgsConstructor
public class CurrencyController {
    private final CurrencyService currencyService;

    @PutMapping("/update-currency/{userId}")
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
}
