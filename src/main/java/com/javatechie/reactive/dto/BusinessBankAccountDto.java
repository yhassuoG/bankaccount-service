package com.javatechie.reactive.dto;

import java.util.List;

import com.javatechie.reactive.entity.BankAccountCorriente;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessBankAccountDto {

    private String id;
    private String firstName;
    private String lastName;
    private long dni;
    private List<BankAccountCorriente> bankAccountCorriente;

}
