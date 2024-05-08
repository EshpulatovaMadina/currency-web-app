package com.devops.service.impl;

import com.devops.DTOS.CurrencyReadDto;
import com.devops.config.Constants;
import com.devops.entity.Currency;
import com.devops.enumaration.CurrencyStrategyName;
import com.devops.service.commend.GetCurrency;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GetCurrencyUsingRestTemplate implements GetCurrency {

    private final RestTemplate restTemplate;

    @Override
    public List<Currency> getAllCurrency() {
        CurrencyReadDto[] currencyList = restTemplate.getForObject(Constants.GET_CURRENCY_URL, CurrencyReadDto[].class);
        assert currencyList != null;
        List<CurrencyReadDto> currencies = new ArrayList<>(Arrays.asList(currencyList));

        currencies.add(
                new CurrencyReadDto(
                        76L,
                        "0",
                        "UZS",
                        "O'zbek so'mi",
                        1D,
                        LocalDate.now()
                ));
        return currencies.stream()
                .map(data -> Currency.builder()
                        .id(data.getBankId())
                        .rate(data.getRate())
                        .code(Integer.valueOf(data.getCode()))
                        .ccy(data.getCcy())
                        .ccyNmUz(data.getCcyNmUz())
                        .date(data.getDate())
                        .build())
                .toList();
    }

    @Override
    public String getStrategyName() {
        return CurrencyStrategyName.CURRENCY_WITH_REST_TEMPLATE.name();
    }

}
