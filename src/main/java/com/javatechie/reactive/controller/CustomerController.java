package com.javatechie.reactive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javatechie.reactive.entity.Customer;
import com.javatechie.reactive.service.CustomerService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
public class CustomerController {

	@Autowired
	private CustomerService service;
	
	@GetMapping
    public Flux<Customer> getCustomers(){
        return service.getCustomers();
    }

    @GetMapping("/{id}")
    public Mono<Customer> getCustomer(@PathVariable String id){
        return service.getCustomer(id);
    }


    @PostMapping
    public Mono<Customer> saveCustomer(@RequestBody Mono<Customer> customer){
        return service.saveCustomer(customer);
    }
}
