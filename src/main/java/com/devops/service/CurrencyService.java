package com.devops.service;

import com.devops.DTOS.CurrencyResponseDto;
import com.devops.entity.Currency;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CurrencyService {
    void saveCurrency();

    List<Currency> getAllCurrency();

    Mono<String> updateData(Long userId);

    Mono<Double> convertCurrency(String fromCurrency, String toCurrency, Double amount);

    Mono<CurrencyResponseDto> getByCode(Integer code);

    Mono<CurrencyResponseDto> getByCcy(String ccy);

    void updateCurrencies();
}
