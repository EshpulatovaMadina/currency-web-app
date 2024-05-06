package com.devops.repository;

import com.devops.entity.Currency;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CurrencyRepository extends ReactiveCrudRepository<Currency, Long> {

    Mono<Currency> findByCode(Integer code);

    Mono<Currency> findByCcy(String ccy);
}
