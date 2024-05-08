package com.devops.service.commend;

import org.apache.commons.collections4.Get;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class CommendContainer {
    private final Map<String, GetCurrency> commendsMap = new HashMap<>();

    public CommendContainer(List<GetCurrency> commends) {
        commends.forEach((executeCommend -> commendsMap.put(executeCommend.getStrategyName(), executeCommend)));
    }

    public GetCurrency getCurrencyService(String getStrategyName ) {
        return commendsMap.getOrDefault(getStrategyName,null);
    }
}
