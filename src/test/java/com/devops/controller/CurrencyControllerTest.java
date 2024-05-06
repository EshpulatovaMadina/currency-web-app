package com.devops.controller;

import com.devops.DTOS.CurrencyResponseDto;
import com.devops.service.CurrencyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.*;
import static reactor.core.publisher.Mono.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CurrencyControllerTest {

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private CurrencyController currencyController;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void updateCurrencyTest() {
        when(currencyService.updateData(anyLong())).thenReturn(Mono.just("Currency database updated ✅"));
        webTestClient.put().uri("/currency/update-currency/123")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Currency database updated ✅");
    }

    @Test
    public void testCurrencyConvertEndpoint() {
        BigDecimal amount = BigDecimal.valueOf(100);
        when(currencyService.convertCurrency(anyString(),anyString(),any(Double.class)))
                .thenReturn(Mono.just(93.43177921575135));

        webTestClient.get().uri("/currency/convert?fromCurrency=USD&toCurrency=EUR&amount=100")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Double.class).isEqualTo(93.43177921575135);
    }

    @Test
    public void testGetCurrencyByCodeEndpoint() {
        CurrencyResponseDto currencyResponseDto = new CurrencyResponseDto(
                1L,
                1L,
                0,
                "UZB",
                "O'zbek so'mi",
                1D,
                LocalDate.now()
        );
        when(currencyService.getByCode(anyInt())).thenReturn(Mono.just(currencyResponseDto));

        webTestClient.get().uri("/currency/get-by-code?code=0")
                .exchange()
                .expectStatus().isOk()
                .expectBody(CurrencyResponseDto.class).value(currencyResponseDto1 -> Objects.equals(currencyResponseDto1,currencyResponseDto));
    }

    @Test
    public void testGetCurrencyByCcyEndpoint() {
        CurrencyResponseDto currencyResponseDto = new CurrencyResponseDto(
                1L,
                69L,
                840,
                "USD",
                "AQSH dollari",
                12670.05,
                LocalDate.parse("2024-05-03")
        );
        when(currencyService.getByCcy(anyString())).thenReturn(Mono.just(currencyResponseDto));

        webTestClient.get().uri("/currency/get-by-ccy?ccy=USD")
                .exchange()
                .expectStatus().isOk()
                .expectBody(CurrencyResponseDto.class).value(currencyResponseDto1 -> Objects.equals(currencyResponseDto1,currencyResponseDto));
    }


}
