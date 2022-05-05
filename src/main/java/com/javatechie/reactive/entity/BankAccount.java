package com.javatechie.reactive.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "bankaccounts")
public class BankAccount {

	@Id
    private String id;
    private double saldo;
    private double pan;
    private int cvv;
    private int movement;
    private String typeBankAccount;
    private List<Customer> customer;
    private String movementDate;
    
    
    private final double COMMISSION_CORRIENTE=30;
    private final int MOVEMENT_LIMIT_AHORRO=10;
    private final int MOVEMENT_LIMIT_PLAZO_FIJO=1;

}
