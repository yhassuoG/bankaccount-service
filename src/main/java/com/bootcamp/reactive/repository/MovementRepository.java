package com.bootcamp.reactive.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.bootcamp.reactive.common.Movement;

@Repository
public interface MovementRepository extends ReactiveMongoRepository<Movement,String>{

}
