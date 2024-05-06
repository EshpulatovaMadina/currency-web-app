package com.devops.service;

import com.devops.DTOS.CurrencyResponseDto;
import com.devops.entity.Currency;
import com.devops.exceptions.DataNotFoundException;
import com.devops.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class CurrencyServiceTest {

    @Mock
    private CurrencyRepository currencyRepository;

    @InjectMocks
    private CurrencyService currencyService;
    Currency currency = new Currency();
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        currency.setId(1L);
        currency.setBankId(123L);
        currency.setCode(123);
        currency.setCcy("USD");
        currency.setCcyNmUz("US Dollar");
        currency.setRate(1.0);
        currency.setDate(LocalDate.parse("2024-05-03"));
    }


    @Test
    public void testSaveCurrency() {
        List<Currency> currencies = new ArrayList<>();
        Mockito.when(currencyRepository.saveAll(Mockito.anyIterable())).thenReturn(Flux.fromIterable(currencies));

        currencyService.saveCurrency();

        Mockito.verify(currencyRepository, Mockito.times(1)).saveAll(Mockito.anyIterable());
    }

    @Test
    public void convertCurrencyTest() {
        Currency sourceCurrency = new Currency();
        sourceCurrency.setCcy("USD");
        sourceCurrency.setRate(1.0);

        Currency targetCurrency = new Currency();
        targetCurrency.setCcy("EUR");
        targetCurrency.setRate(0.85);

        Double amount = 100D;

        when(currencyRepository.findByCcy("USD")).thenReturn(Mono.just(sourceCurrency));
        when(currencyRepository.findByCcy("EUR")).thenReturn(Mono.just(targetCurrency));

        Mono<Double> result = currencyService.convertCurrency("USD", "EUR", amount);

        StepVerifier.create(result)
                .expectNextMatches(convertedAmount -> {
                    double expectedConvertedAmount = 117.64705882352942;
                    return convertedAmount.equals(expectedConvertedAmount);
                })
                .verifyComplete();

        verify(currencyRepository, times(1)).findByCcy("USD");
        verify(currencyRepository, times(1)).findByCcy("EUR");
    }


    @Test
    public void testGetByCode() {
        when(currencyRepository.findByCode(123)).thenReturn(Mono.just(currency));
        Mono<CurrencyResponseDto> resultMono = currencyService.getByCode(123);

        CurrencyResponseDto result = resultMono.block();
        assertEquals("USD", result.getCcy());
    }

    @Test
    public void testGetByCodeNotFound() {
        when(currencyRepository.findByCode(any(Integer.class))).thenReturn(Mono.empty());

        Mono<CurrencyResponseDto> resultMono = currencyService.getByCode(999);

        assertThrows(DataNotFoundException.class, resultMono::block);
    }

    @Test
    public void testGetByCcy() {
        when(currencyRepository.findByCcy("USD")).thenReturn(Mono.just(currency));

        Mono<CurrencyResponseDto> resultMono = currencyService.getByCcy("USD");

        CurrencyResponseDto result = resultMono.block();
        assertEquals("USD", result.getCcy());
    }

    @Test
    public void testGetByCcyNotFound() {
        when(currencyRepository.findByCcy(any(String.class))).thenReturn(Mono.empty());

        Mono<CurrencyResponseDto> resultMono = currencyService.getByCcy("XYZ");

        assertThrows(DataNotFoundException.class, resultMono::block);
    }

}
