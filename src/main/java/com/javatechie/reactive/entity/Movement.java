package com.javatechie.reactive.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "movimientos")
public class Movement {

	private String id;
	private String movementDate;
	private String type;
	private double amount;
	
}
