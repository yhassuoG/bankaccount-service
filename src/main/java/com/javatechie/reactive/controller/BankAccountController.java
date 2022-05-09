package com.javatechie.reactive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javatechie.reactive.dto.AccountDto;
import com.javatechie.reactive.dto.AccountMovementDto;
import com.javatechie.reactive.dto.BankAccountDto;
import com.javatechie.reactive.dto.OperationDto;
import com.javatechie.reactive.dto.PersonalBankAccountDto;
import com.javatechie.reactive.entity.BankAccountAhorro;
import com.javatechie.reactive.entity.BankAccountCorriente;
import com.javatechie.reactive.entity.BankAccountPlazoFijo;
import com.javatechie.reactive.entity.CustomerPersonal;
import com.javatechie.reactive.service.BankAccountService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/bankaccounts")
public class BankAccountController {

	@Autowired
	private BankAccountService service;
	
	/*@GetMapping
    public Flux<Customer> getCustomers(){
        return service.getCustomers();
    }

    @GetMapping("/{id}")
    public Mono<Customer> getCustomer(@PathVariable String id){
        return service.getCustomer(id);
    }*/
	@GetMapping("/personal/{dni}")
    public Mono<PersonalBankAccountDto> getPersonalBankAccounts(@PathVariable long dni){
        return service.getPersonalBankAccounts(dni);
    }
	@PostMapping("/movimientos")
    public Mono<AccountMovementDto> getMovementsBankAccounts(@RequestBody Mono<AccountDto> accountdto){
        return service.getMovementsBankAccounts(accountdto);
    }

    @PostMapping("/ahorro")
    public Mono<CustomerPersonal> saveBankAccountAhorro(@RequestBody Mono<BankAccountDto> bankaccountdto){
        return service.saveBankAccountAhorro(bankaccountdto);
    }
    
    @PostMapping("/corriente")
    public Mono<?> saveBankAccountCorriente(@RequestBody Mono<BankAccountDto> bankaccountdto){
        return service.saveBankAccountCorriente(bankaccountdto);
    }
    
    @PostMapping("/plazofijo")
    public Mono<CustomerPersonal> saveBankAccountPlazoFijo(@RequestBody Mono<BankAccountDto> bankaccountdto){
        return service.saveBankAccountPlazoFijo(bankaccountdto);
    }
    
    @PostMapping("/ahorro/operacion")
    public Mono<BankAccountAhorro> doOperationAhorro(@RequestBody Mono<OperationDto> depositDto){
    	return service.doOperationAhorro(depositDto);
    }
    
    @PostMapping("/corriente/operacion")
    public Mono<BankAccountCorriente> doOperationCorriente(@RequestBody Mono<OperationDto> depositDto){
    	return service.doOperationCorriente(depositDto);
    }
    
    @PostMapping("/plazofijo/operacion")
    public Mono<BankAccountPlazoFijo> doOperationPlazoFijo(@RequestBody Mono<OperationDto> depositDto){
    	return service.doOperationPlazoFijo(depositDto);
    }
}
