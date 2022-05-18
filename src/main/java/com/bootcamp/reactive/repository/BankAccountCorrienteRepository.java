package com.bootcamp.reactive.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.bootcamp.reactive.entity.BankAccountAhorro;
import com.bootcamp.reactive.entity.BankAccountCorriente;

import reactor.core.publisher.Mono;

@Repository
public interface BankAccountCorrienteRepository extends ReactiveMongoRepository<BankAccountCorriente,String> {

	Mono<BankAccountCorriente> findByPan(long pan);
}
