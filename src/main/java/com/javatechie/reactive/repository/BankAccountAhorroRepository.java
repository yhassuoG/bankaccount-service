package com.javatechie.reactive.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.javatechie.reactive.entity.BankAccountAhorro;
import com.javatechie.reactive.entity.CustomerPersonal;

import reactor.core.publisher.Mono;

@Repository
public interface BankAccountAhorroRepository extends ReactiveMongoRepository<BankAccountAhorro,String>{

	Mono<BankAccountAhorro> findByPan(long pan);

}
