package com.bootcamp.reactive.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movement {

	private String id;
	private String movementDate;
	private String type;
	private double amount;
	
}
