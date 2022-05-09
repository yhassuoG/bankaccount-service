package com.javatechie.reactive.dto;

import java.util.List;

import com.javatechie.reactive.entity.BankAccountAhorro;
import com.javatechie.reactive.entity.BankAccountCorriente;
import com.javatechie.reactive.entity.BankAccountPlazoFijo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountDto {

	private CustomerDto customer;
	private List<BankAccountCorriente> bankAccountCorriente;
    private BankAccountAhorro bankAccountAhorro ;
    private BankAccountPlazoFijo bankAccountPlazoFijo;
	
}
