package com.javatechie.reactive.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "business_customers")
public class CustomerBusiness {

	@Id
    private String id;
    private String firstName;
    private String lastName;
    private long dni;
    private long phoneNumber;
    private List<String> bankAccountCorriente;
}
