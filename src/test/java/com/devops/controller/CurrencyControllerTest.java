package com.devops.controller;

import com.devops.DTOS.CurrencyResponseDto;
import com.devops.arguments.CurrencyRequestParam;
import com.devops.entity.Currency;
import com.devops.enumaration.CurrencyStrategyName;
import com.devops.service.CurrencyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static reactor.core.publisher.Mono.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CurrencyControllerTest {

    @SpyBean
    private CurrencyService currencyService;

    @Autowired
    private WebTestClient webTestClient;

    private static final String GET_ALL_CURRENCY_API = "/currency/get-all";

    @Test
    public void updateCurrencyTest() {
        when(currencyService.updateData(anyLong())).thenReturn(Mono.just("Currency database updated ✅"));
        webTestClient.put().uri("/currency/update/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Currency database updated ✅");
    }

    @ParameterizedTest
    @MethodSource("getCurrencyParams")
    public void testCurrencyConvertEndpoint(CurrencyRequestParam requestParam, Double expectedAmountInDouble) {
        when(currencyService.convertCurrency(anyString(),anyString(),any(Double.class)))
                .thenReturn(Mono.just(expectedAmountInDouble));

        webTestClient.get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/currency/convert")
                                .queryParam("fromCurrency", requestParam.getFromCurrency())
                                .queryParam("toCurrency", requestParam.getToCurrency())
                                .queryParam("amount", requestParam.getAmount())
                                .build()
                )
                .exchange()
                .expectStatus().isOk()
                .expectBody(Double.class).isEqualTo(expectedAmountInDouble);
    }

    private static List<Arguments> getCurrencyParams() {
        return List.of(
                Arguments.of(
                        new CurrencyRequestParam(
                                "USD","EUR", 100D
                        ),
                        92.93682503698352
                )
        );
    }

    @Test
    public void testGetAllCurrencies() {
        List<Currency> currencyList = webTestClient.get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path(GET_ALL_CURRENCY_API)
                                .queryParam("getCurrencyStrategy", CurrencyStrategyName.CURRENCY_WITH_REST_TEMPLATE.name())
                                .build()
                )
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json")
                .returnResult(Currency.class)
                .getResponseBody()
                .collectList()
                .block();

        assertNotNull(currencyList);
        System.out.println("RESULT DATA: " + currencyList);
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
