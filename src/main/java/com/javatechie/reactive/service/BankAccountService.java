package com.javatechie.reactive.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.javatechie.reactive.dto.AccountDto;
import com.javatechie.reactive.dto.AccountMovementDto;
import com.javatechie.reactive.dto.BankAccountDto;
import com.javatechie.reactive.dto.CustomerDto;
import com.javatechie.reactive.dto.OperationDto;
import com.javatechie.reactive.dto.PersonalBankAccountDto;
import com.javatechie.reactive.entity.BankAccountAhorro;
import com.javatechie.reactive.entity.BankAccountCorriente;
import com.javatechie.reactive.entity.BankAccountPlazoFijo;
import com.javatechie.reactive.entity.CustomerBusiness;
import com.javatechie.reactive.entity.CustomerPersonal;
import com.javatechie.reactive.entity.Movement;
import com.javatechie.reactive.repository.BankAccountAhorroRepository;
import com.javatechie.reactive.repository.BankAccountCorrienteRepository;
import com.javatechie.reactive.repository.BankAccountPlazoFijoRepository;
import com.javatechie.reactive.repository.CustomerBusinessRepository;
import com.javatechie.reactive.repository.CustomerPersonalRepository;
import com.javatechie.reactive.repository.MovementRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class BankAccountService {

	@Autowired
	private BankAccountAhorroRepository bankAhorroRepository;

	@Autowired
	private BankAccountCorrienteRepository bankCorrienteRepository;

	@Autowired
	private BankAccountPlazoFijoRepository bankPlazoFijoRepository;

	@Autowired
	private CustomerPersonalRepository customerRepository;

	@Autowired
	private CustomerBusinessRepository customerBusinessRepository;

	@Autowired
	private MovementRepository movementRepository;

	/*
	 * public Flux<BankAccount> getBankAccountsByCustomer(String id){ return
	 * repository.findAllByCustomer(); }
	 */
	public Mono<PersonalBankAccountDto> getPersonalBankAccounts(long dni) {

		// No olvidar ; luego del )

		return customerRepository.findByDni(dni).flatMap(customer -> {
			if (customer.getBankAccountAhorro() != null) {
				return bankAhorroRepository.findById(customer.getBankAccountAhorro()).map(bankAccount -> {
					PersonalBankAccountDto accounts = new PersonalBankAccountDto();
					accounts.setId(customer.getId());
					accounts.setFirstName(customer.getFirstName());
					accounts.setLastName(customer.getLastName());
					accounts.setDni(customer.getDni());
					accounts.setBankAccountAhorro(bankAccount);

					return accounts;
				});
			} else if (customer.getBankAccountCorriente() != null) {
				return bankCorrienteRepository.findById(customer.getBankAccountCorriente()).map(bankAccount -> {
					PersonalBankAccountDto accounts = new PersonalBankAccountDto();
					accounts.setId(customer.getId());
					accounts.setFirstName(customer.getFirstName());
					accounts.setLastName(customer.getLastName());
					accounts.setDni(customer.getDni());
					accounts.setBankAccountCorriente(bankAccount);
					return accounts;
				});
			} else if (customer.getBankAccountPlazoFijo() != null) {
				return bankPlazoFijoRepository.findById(customer.getBankAccountCorriente()).map(bankAccount -> {
					PersonalBankAccountDto accounts = new PersonalBankAccountDto();
					accounts.setId(customer.getId());
					accounts.setFirstName(customer.getFirstName());
					accounts.setLastName(customer.getLastName());
					accounts.setDni(customer.getDni());
					accounts.setBankAccountPlazoFijo(bankAccount);
					return accounts;
				});
			}
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		});
	}

	public Mono<AccountMovementDto> getMovementsBankAccounts(Mono<AccountDto> accountDto) {

		return accountDto.flatMap(acDto -> {

			if (acDto.getType().equalsIgnoreCase("ahorro")) {

				return bankAhorroRepository.findByPan(acDto.getPan()).flatMap(bankAccount -> {

					AccountMovementDto accmov = new AccountMovementDto();
					accmov.setPan(bankAccount.getPan());
					accmov.setMovements(new ArrayList<>());
					List<Movement> movs = new ArrayList<>();

					return Flux.fromIterable(bankAccount.getMovements()).flatMap(movementRepository::findById)
							.collectList().map(movements -> {
								accmov.setMovements(movements);
								return accmov;
							});

				});
			} else if (acDto.getType().equalsIgnoreCase("corriente")) {

				return bankCorrienteRepository.findByPan(acDto.getPan()).flatMap(bankAccount -> {

					AccountMovementDto accmov = new AccountMovementDto();
					accmov.setPan(bankAccount.getPan());
					accmov.setMovements(new ArrayList<>());
					List<Movement> movs = new ArrayList<>();

					return Flux.fromIterable(bankAccount.getMovements()).flatMap(movementRepository::findById)
							.collectList().map(movements -> {
								accmov.setMovements(movements);
								return accmov;
							});

				});
			} else if (acDto.getType().equalsIgnoreCase("plazofijo")) {
				return bankPlazoFijoRepository.findByPan(acDto.getPan()).flatMap(bankAccount -> {

					AccountMovementDto accmov = new AccountMovementDto();
					accmov.setPan(bankAccount.getPan());
					accmov.setMovements(new ArrayList<>());
					List<Movement> movs = new ArrayList<>();

					return Flux.fromIterable(bankAccount.getMovements()).flatMap(movementRepository::findById)
							.collectList().map(movements -> {
								accmov.setMovements(movements);
								return accmov;
							});

				});
			}
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		});
	}

	public Mono<CustomerPersonal> saveBankAccountAhorro(Mono<BankAccountDto> bankAccountDto) {
		return bankAccountDto.flatMap(e -> {

			if (e.getCustomer().getType().equalsIgnoreCase("personal")) {

				CustomerDto customerDto = e.getCustomer();
				BankAccountAhorro accountAhorro = e.getBankAccountAhorro();
				accountAhorro.setMovements(new ArrayList<>());

				CustomerPersonal customer = new CustomerPersonal();
				customer.setFirstName(customerDto.getFirstName());
				customer.setLastName(customerDto.getLastName());
				customer.setDni(customerDto.getDni());
				customer.setPhoneNumber(customerDto.getPhoneNumber());

				return bankAhorroRepository.insert(accountAhorro).flatMap(bankAccount -> {
					customer.setBankAccountAhorro(bankAccount.getId());
					return customerRepository.insert(customer);
				});

			}
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		});
	}

//
	public Mono<?> saveBankAccountCorriente(Mono<BankAccountDto> bankAccountDto) {
		return bankAccountDto.flatMap(e -> {
			if (e.getCustomer().getType().equalsIgnoreCase("personal")) {

				CustomerDto customerDto = e.getCustomer();
				List<BankAccountCorriente> accountCorriente = e.getBankAccountCorriente().stream().map(ac -> {

					ac.setMovements(new ArrayList<>());
					return ac;
				}).collect(Collectors.toList());

				CustomerPersonal customer = new CustomerPersonal();
				customer.setFirstName(customerDto.getFirstName());
				customer.setLastName(customerDto.getLastName());
				customer.setDni(customerDto.getDni());
				customer.setPhoneNumber(customerDto.getPhoneNumber());

				return bankCorrienteRepository.insert(accountCorriente.get(0)).flatMap(bankAccount -> {
					customer.setBankAccountCorriente(bankAccount.getId());
					return customerRepository.insert(customer);
				});

			} else if (e.getCustomer().getType().equalsIgnoreCase("empresarial")) {

				CustomerDto customerDto = e.getCustomer();
				List<BankAccountCorriente> accountCorriente = e.getBankAccountCorriente().stream().map(ac -> {

					ac.setMovements(new ArrayList<>());
					return ac;
				}).collect(Collectors.toList());

				CustomerBusiness customer = new CustomerBusiness();
				customer.setFirstName(customerDto.getFirstName());
				customer.setLastName(customerDto.getLastName());
				customer.setDni(customerDto.getDni());
				customer.setPhoneNumber(customerDto.getPhoneNumber());

				return bankCorrienteRepository.insert(e.getBankAccountCorriente()).collectList()
						.flatMap(bankAccounts -> {
							customer.setBankAccountCorriente(
									bankAccounts.stream().map(ac -> ac.getId()).collect(Collectors.toList()));
							return customerBusinessRepository.insert(customer);
						});
			}

			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		});
	}
	
	public Mono<CustomerPersonal> saveBankAccountPlazoFijo(Mono<BankAccountDto> bankAccountDto) {
		return bankAccountDto.flatMap(e -> {

			if (e.getCustomer().getType().equalsIgnoreCase("personal")) {

				CustomerDto customerDto = e.getCustomer();
				BankAccountPlazoFijo accountPlazoFijo = e.getBankAccountPlazoFijo();
				accountPlazoFijo.setMovements(new ArrayList<>());

				CustomerPersonal customer = new CustomerPersonal();
				customer.setFirstName(customerDto.getFirstName());
				customer.setLastName(customerDto.getLastName());
				customer.setDni(customerDto.getDni());
				customer.setPhoneNumber(customerDto.getPhoneNumber());

				return bankPlazoFijoRepository.insert(accountPlazoFijo).flatMap(bankAccount -> {
					customer.setBankAccountPlazoFijo(bankAccount.getId());
					return customerRepository.insert(customer);
				});

			}
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		});
	}

	public Mono<BankAccountAhorro> doOperationAhorro(Mono<OperationDto> operationDto) {
		return operationDto.flatMap(e -> {

			if (e.getType().equalsIgnoreCase("deposito")) {
				Movement movement = new Movement();
				movement.setMovementDate(LocalDateTime.now().toString());
				movement.setType(e.getType());
				movement.setAmount(e.getAmount());
				return movementRepository.insert(movement).flatMap(m -> {
					return bankAhorroRepository.findByPan(e.getPan()).flatMap(bankAccount -> {
						List<String> movs = bankAccount.getMovements();
						movs.add(m.getId());
						bankAccount.setMovements(movs);
						bankAccount.setSaldo(bankAccount.getSaldo() + e.getAmount());
						return bankAhorroRepository.save(bankAccount);
					});
				});

			} else if (e.getType().equalsIgnoreCase("retiro")) {

				Movement movement = new Movement();
				movement.setMovementDate(LocalDateTime.now().toString());
				movement.setType(e.getType());
				movement.setAmount(e.getAmount());
				return bankAhorroRepository.findByPan(e.getPan()).flatMap(bankAccount -> {
					if (bankAccount.getSaldo() - e.getAmount() >= 0) {
						return movementRepository.insert(movement).flatMap(m -> {

							List<String> movs = bankAccount.getMovements();
							movs.add(m.getId());
							bankAccount.setMovements(movs);
							bankAccount.setSaldo(bankAccount.getSaldo() - e.getAmount());
							return bankAhorroRepository.save(bankAccount);

						});
					}
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
				});

			}

			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		});
	}

	public Mono<BankAccountCorriente> doOperationCorriente(Mono<OperationDto> operationDto) {
		return operationDto.flatMap(e -> {

			if (e.getType().equalsIgnoreCase("deposito")) {
				Movement movement = new Movement();
				movement.setMovementDate(LocalDateTime.now().toString());
				movement.setType(e.getType());
				movement.setAmount(e.getAmount());
				return movementRepository.insert(movement).flatMap(m -> {
					return bankCorrienteRepository.findByPan(e.getPan()).flatMap(bankAccount -> {
						List<String> movs = bankAccount.getMovements();
						movs.add(m.getId());
						bankAccount.setMovements(movs);
						bankAccount.setSaldo(bankAccount.getSaldo() + e.getAmount());
						return bankCorrienteRepository.save(bankAccount);
					});
				});

			} else if (e.getType().equalsIgnoreCase("retiro")) {

				Movement movement = new Movement();
				movement.setMovementDate(LocalDateTime.now().toString());
				movement.setType(e.getType());
				movement.setAmount(e.getAmount());
				return bankCorrienteRepository.findByPan(e.getPan()).flatMap(bankAccount -> {
					if (bankAccount.getSaldo() - e.getAmount() >= 0) {
						return movementRepository.insert(movement).flatMap(m -> {

							List<String> movs = bankAccount.getMovements();
							movs.add(m.getId());
							bankAccount.setMovements(movs);
							bankAccount.setSaldo(bankAccount.getSaldo() - e.getAmount());
							return bankCorrienteRepository.save(bankAccount);

						});
					}
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
				});

			}

			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		});
	}
	
	public Mono<BankAccountPlazoFijo> doOperationPlazoFijo(Mono<OperationDto> operationDto) {
		return operationDto.flatMap(e -> {

			if (e.getType().equalsIgnoreCase("deposito")) {
				Movement movement = new Movement();
				movement.setMovementDate(LocalDateTime.now().toString());
				movement.setType(e.getType());
				movement.setAmount(e.getAmount());
				return movementRepository.insert(movement).flatMap(m -> {
					return bankPlazoFijoRepository.findByPan(e.getPan()).flatMap(bankAccount -> {
						List<String> movs = bankAccount.getMovements();
						movs.add(m.getId());
						bankAccount.setMovements(movs);
						bankAccount.setSaldo(bankAccount.getSaldo() + e.getAmount());
						return bankPlazoFijoRepository.save(bankAccount);
					});
				});

			} else if (e.getType().equalsIgnoreCase("retiro")) {

				Movement movement = new Movement();
				movement.setMovementDate(LocalDateTime.now().toString());
				movement.setType(e.getType());
				movement.setAmount(e.getAmount());
				return bankPlazoFijoRepository.findByPan(e.getPan()).flatMap(bankAccount -> {
					if (bankAccount.getSaldo() - e.getAmount() >= 0) {
						return movementRepository.insert(movement).flatMap(m -> {

							List<String> movs = bankAccount.getMovements();
							movs.add(m.getId());
							bankAccount.setMovements(movs);
							bankAccount.setSaldo(bankAccount.getSaldo() - e.getAmount());
							return bankPlazoFijoRepository.save(bankAccount);

						});
					}
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
				});

			}

			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		});
	}
}
