package com.bootcamp.reactive.dto;

import java.util.List;

import com.bootcamp.reactive.entity.BankAccountAhorro;
import com.bootcamp.reactive.entity.BankAccountCorriente;
import com.bootcamp.reactive.entity.BankAccountPlazoFijo;

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
