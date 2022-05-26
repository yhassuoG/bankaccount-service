package com.bootcamp.reactive.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;

import com.bootcamp.reactive.common.CustomerPersonal;
import com.bootcamp.reactive.dto.BankAccountDto;
import com.bootcamp.reactive.dto.CustomerDto;
import com.bootcamp.reactive.entity.BankAccountAhorro;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
class BankAccountServiceTest {
	
	@Autowired
	BankAccountService service;
 

	@Test
	public void when_saveBankAccountAhorro_ok() {
				
		//ESTO SE LE MANDA AL SERVICE
		CustomerDto customer = new CustomerDto();
		customer.setFirstName("Raul");
		customer.setLastName("Castro");
		customer.setDni(76865456);
		customer.setPhoneNumber(939393239);
		customer.setType("personal");
		BankAccountAhorro accountAhorro = new BankAccountAhorro();
		accountAhorro.setPan(1849233114578598L);
		accountAhorro.setCvv(123);
		accountAhorro.setSaldo(0);
		accountAhorro.setMovements(new ArrayList<>());
		BankAccountDto accountDto = new BankAccountDto();
		accountDto.setCustomer(customer);
		accountDto.setBankAccountAhorro(accountAhorro);
		
		//ESTO DEBE RETORNAR EL SERVICE
		CustomerPersonal customerPersonal = new CustomerPersonal();
		customerPersonal.setFirstName("Raul");
		customerPersonal.setLastName("Castro");
		customerPersonal.setDni(76865456);
		customerPersonal.setPhoneNumber(939393239);
		StepVerifier.create(service.saveBankAccountAhorro(Mono.just(accountDto)))
			.expectNext(customerPersonal)
			.expectComplete()
			.verify();
	}

}
