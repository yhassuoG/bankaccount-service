package com.javatechie.reactive.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.javatechie.reactive.entity.CustomerBusiness;
import com.javatechie.reactive.entity.CustomerPersonal;

@Repository
public interface CustomerBusinessRepository extends ReactiveMongoRepository<CustomerBusiness,String>{

}
