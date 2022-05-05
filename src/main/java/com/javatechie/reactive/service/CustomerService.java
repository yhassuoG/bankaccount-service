package com.javatechie.reactive.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javatechie.reactive.entity.Customer;
import com.javatechie.reactive.repository.CustomerRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository repository;
	
	public Flux<Customer> getCustomers(){
        return repository.findAll();
    }

    public Mono<Customer> getCustomer(String id){
        return repository.findById(id);
    }
    
	public Mono<Customer> saveCustomer(Mono<Customer> customer){
        System.out.println("Creando cliente ...");
      return  customer.flatMap(repository::insert);
    }
	
	
}
