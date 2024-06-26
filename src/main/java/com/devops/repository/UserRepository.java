package com.devops.repository;

import com.devops.entity.User;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User,Long> {
    Mono<User> findFirstByEmail(String email);

    @Modifying
    Mono<Void> deleteByEmail(String email);
}
