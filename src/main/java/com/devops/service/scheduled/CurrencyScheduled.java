package com.devops.service.scheduled;

import com.devops.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrencyScheduled {

    private final CurrencyService currencyService;

    @Scheduled(cron = "0 0 0 * * *")
    public void updateCurrencyScheduled() {
        currencyService.updateCurrencies();
    }
}
