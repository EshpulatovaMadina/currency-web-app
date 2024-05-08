package com.devops.client;

import com.devops.DTOS.CurrencyReadDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import static com.devops.config.Constants.GET_CURRENCY_URL;

@FeignClient(value = "currency-service", url = GET_CURRENCY_URL)
public interface CurrencyFeignClient {

    @GetMapping
    List<CurrencyReadDto> getAllCurrency();

}
