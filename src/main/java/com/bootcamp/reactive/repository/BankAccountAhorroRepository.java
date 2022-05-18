package com.bootcamp.reactive.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.bootcamp.reactive.entity.BankAccountAhorro;

import reactor.core.publisher.Mono;

@Repository
public interface BankAccountAhorroRepository extends ReactiveMongoRepository<BankAccountAhorro,String>{

	Mono<BankAccountAhorro> findByPan(long pan);

}
