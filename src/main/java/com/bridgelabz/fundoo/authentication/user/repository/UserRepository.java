package com.bridgelabz.fundoo.authentication.user.repository;

import com.bridgelabz.fundoo.authentication.user.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User,Long> {
    Mono<Boolean>existsByEmailId(String emailId);
    Mono<User>findByEmailId(String emailId);
}
