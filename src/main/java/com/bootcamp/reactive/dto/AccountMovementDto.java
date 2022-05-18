package com.bootcamp.reactive.dto;

import java.util.List;

import com.bootcamp.reactive.common.Movement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountMovementDto {

	private long pan;
    private List<Movement> movements;
}
