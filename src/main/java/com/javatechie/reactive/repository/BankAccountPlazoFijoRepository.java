package com.javatechie.reactive.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.javatechie.reactive.entity.BankAccountPlazoFijo;

import reactor.core.publisher.Mono;

@Repository
public interface BankAccountPlazoFijoRepository extends ReactiveMongoRepository<BankAccountPlazoFijo,String>{

	Mono<BankAccountPlazoFijo> findByPan(long pan);

}
