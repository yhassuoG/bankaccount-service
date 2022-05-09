package com.javatechie.reactive.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.javatechie.reactive.dto.PersonalBankAccountDto;
import com.javatechie.reactive.entity.CustomerPersonal;

import reactor.core.publisher.Mono;

@Repository
public interface CustomerPersonalRepository extends ReactiveMongoRepository<CustomerPersonal,String>{

	Mono<CustomerPersonal> findByDni(long dni);

}
