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
@Document(collection = "customers")
public class Customer {

	@Id
    private String id;
    private String firstName;
    private String lastName;
    private long dni;
    private long phoneNumber;
    private String typeCustomer;
    //private List<BankAccount> bankAccounts;
}
