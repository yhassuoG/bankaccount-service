package com.bootcamp.reactive.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerPersonal {

    private String id;
    private String firstName;
    private String lastName;
    private long dni;
    private long phoneNumber;
    private String bankAccountCorriente;
    private String bankAccountAhorro;
    private String bankAccountPlazoFijo;
    
}
