package com.bootcamp.reactive.dto;

import java.util.List;

import com.bootcamp.reactive.entity.BankAccountAhorro;
import com.bootcamp.reactive.entity.BankAccountCorriente;
import com.bootcamp.reactive.entity.BankAccountPlazoFijo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {

	private String id;
    private String firstName;
    private String lastName;
    private long dni;
    private long phoneNumber;
    private String type;
    
}
