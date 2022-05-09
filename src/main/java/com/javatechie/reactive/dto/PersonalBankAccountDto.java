package com.javatechie.reactive.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.javatechie.reactive.entity.BankAccountAhorro;
import com.javatechie.reactive.entity.BankAccountCorriente;
import com.javatechie.reactive.entity.BankAccountPlazoFijo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class PersonalBankAccountDto {

    private String id;
    private String firstName;
    private String lastName;
    private long dni;
    //para personal
    private BankAccountCorriente bankAccountCorriente;
    //empresarial
    private List<BankAccountCorriente> bankAccountsCorriente;
    private BankAccountAhorro bankAccountAhorro;
    private BankAccountPlazoFijo bankAccountPlazoFijo;
}
