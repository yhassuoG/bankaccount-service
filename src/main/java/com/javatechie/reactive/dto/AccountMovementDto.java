package com.javatechie.reactive.dto;

import java.util.List;

import com.javatechie.reactive.entity.Movement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountMovementDto {

	private long pan;
    private List<Movement> movements;
}
