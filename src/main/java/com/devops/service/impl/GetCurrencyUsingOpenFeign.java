package com.devops.service.impl;

import com.devops.DTOS.CurrencyReadDto;
import com.devops.client.CurrencyFeignClient;
import com.devops.entity.Currency;
import com.devops.enumaration.CurrencyStrategyName;
import com.devops.service.commend.GetCurrency;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@RequiredArgsConstructor
@Service
public class GetCurrencyUsingOpenFeign implements GetCurrency {

    private final CurrencyFeignClient currencyFeignClient;
    @Override
    public List<Currency> getAllCurrency() {
        List<CurrencyReadDto> currencies = currencyFeignClient.getAllCurrency();

//        ObjectMapper objectMapper = new ObjectMapper();
//        List<CurrencyReadDto> currencies = null;
//        try {
//            currencies = objectMapper.readValue(
//                    new URL(url),
//                    new com.fasterxml.jackson.core.type.TypeReference<List<CurrencyReadDto>>() {}
//            );
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

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
    public String getStrategyName() {
        return CurrencyStrategyName.CURRENCY_WITH_OPEN_FEIGN.name();
    }
}
