package com.bootcamp.reactive.common;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerBusiness {

    private String id;
    private String firstName;
    private String lastName;
    private long dni;
    private long phoneNumber;
    private List<String> bankAccountCorriente;
}
