package com.javatechie.reactive.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.javatechie.reactive.entity.Customer;

@Repository
public interface CustomerRepository extends ReactiveMongoRepository<Customer,String>{

}
