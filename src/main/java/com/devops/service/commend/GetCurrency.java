package com.devops.service.commend;

import com.devops.entity.Currency;

import java.util.List;

public interface GetCurrency {
    List<Currency> getAllCurrency();
    String getStrategyName();
}
