package com.bootcamp.reactive.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import com.bootcamp.reactive.common.CustomerBusiness;
import com.bootcamp.reactive.common.CustomerPersonal;
import com.bootcamp.reactive.common.Movement;
import com.bootcamp.reactive.dto.AccountDto;
import com.bootcamp.reactive.dto.AccountMovementDto;
import com.bootcamp.reactive.dto.BankAccountDto;
import com.bootcamp.reactive.dto.CustomerDto;
import com.bootcamp.reactive.dto.OperationDto;
import com.bootcamp.reactive.dto.PersonalBankAccountDto;
import com.bootcamp.reactive.entity.BankAccountAhorro;
import com.bootcamp.reactive.entity.BankAccountCorriente;
import com.bootcamp.reactive.entity.BankAccountPlazoFijo;
import com.bootcamp.reactive.repository.BankAccountAhorroRepository;
import com.bootcamp.reactive.repository.BankAccountCorrienteRepository;
import com.bootcamp.reactive.repository.BankAccountPlazoFijoRepository;
import com.bootcamp.reactive.utils.Constantes;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RefreshScope
public class BankAccountService {

	@Autowired
	private BankAccountAhorroRepository bankAhorroRepository;

	@Autowired
	private BankAccountCorrienteRepository bankCorrienteRepository;

	@Autowired
	private BankAccountPlazoFijoRepository bankPlazoFijoRepository;

	@Autowired
	@Lazy
	private WebClient.Builder webClientBuilder;

	@Value("${microservice.movement-service.endpoints.endpoint1.uri}")
	private String ENDPOINT_GET_MOVEMENT;
	
	@Value("${microservice.movement-service.endpoints.endpoint2.uri}")
	private String ENDPOINT_SAVE_MOVEMENT;
	
	@Value("${microservice.customer-service.endpoints.endpoint1.uri}")
	private String ENDPOINT_GET_CUSTOMER;
	
	@Value("${microservice.customer-service.endpoints.endpoint2.uri}")
	private String ENDPOINT_SAVE_CUSTOMER_PERSONAL;
	
	@Value("${microservice.customer-service.endpoints.endpoint3.uri}")
	private String ENDPOINT_SAVE_CUSTOMER_BUSINESS;
	
	public Mono<PersonalBankAccountDto> getPersonalBankAccounts(long dni) {

		// No olvidar ; luego del )
		Mono<CustomerPersonal> cus = webClientBuilder.build().get()
				.uri(ENDPOINT_GET_CUSTOMER, dni).retrieve()
				.bodyToMono(CustomerPersonal.class);
		return cus.flatMap(customer -> {
			System.out.println(customer);
			PersonalBankAccountDto accounts = new PersonalBankAccountDto();
			accounts.setId(customer.getId());
			accounts.setFirstName(customer.getFirstName());
			accounts.setLastName(customer.getLastName());
			accounts.setDni(customer.getDni());

			if (customer.getBankAccountAhorro() != null) {
				return bankAhorroRepository.findById(customer.getBankAccountAhorro()).map(bankAccount -> {

					accounts.setBankAccountAhorro(bankAccount);

					return accounts;
				});
			} else if (customer.getBankAccountCorriente() != null) {
				return bankCorrienteRepository.findById(customer.getBankAccountCorriente()).map(bankAccount -> {

					accounts.setBankAccountCorriente(bankAccount);
					return accounts;
				});
			} else if (customer.getBankAccountPlazoFijo() != null) {
				return bankPlazoFijoRepository.findById(customer.getBankAccountCorriente()).map(bankAccount -> {

					accounts.setBankAccountPlazoFijo(bankAccount);
					return accounts;
				});
			}
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		});
	}

	@CircuitBreaker(name="movementService",fallbackMethod = "movementServiceFallback")
	//@TimeLimiter(name="timelimiterSlow",fallbackMethod = "movementServiceFallback")
	public Mono<AccountMovementDto> getMovementsBankAccounts(Mono<AccountDto> accountDto) {

		return accountDto.flatMap(acDto -> {
			if (acDto.getType().equalsIgnoreCase(Constantes.ACCOUNT_AHORRO)) {

				return bankAhorroRepository.findByPan(acDto.getPan()).flatMap(bankAccount -> {

					AccountMovementDto accmov = new AccountMovementDto();
					accmov.setPan(bankAccount.getPan());
					accmov.setMovements(new ArrayList<>());
					List<Movement> movs = new ArrayList<>();

					return Flux.fromIterable(bankAccount.getMovements()).flatMap(idMov -> {
						Mono<Movement> mov = webClientBuilder.build().get()
								.uri(ENDPOINT_GET_MOVEMENT, idMov).retrieve()
								.bodyToMono(Movement.class);

						return mov;
					}).collectList().map(movements -> {
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

					return Flux.fromIterable(bankAccount.getMovements()).flatMap(idMov -> {
						Mono<Movement> mov = webClientBuilder.build().get()
								.uri(ENDPOINT_GET_MOVEMENT, idMov).retrieve()
								.bodyToMono(Movement.class);

						return mov;
					}).collectList().map(movements -> {
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

					return Flux.fromIterable(bankAccount.getMovements()).flatMap(idMov -> {
						Mono<Movement> mov = webClientBuilder.build().get()
								.uri(ENDPOINT_GET_MOVEMENT, idMov).retrieve()
								.bodyToMono(Movement.class);

						return mov;
					}).collectList().map(movements -> {
						accmov.setMovements(movements);
						return accmov;
					});

				});
			}
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		});
	}
	
	public Mono<AccountMovementDto> movementServiceFallback(Exception ex) {
	
		System.out.println(ex);
		System.out.println("Se acabo el tiempo de espera para la peticion");
		AccountMovementDto response = new AccountMovementDto();
		return Mono.just(response);
	}

	public Mono<CustomerPersonal> saveBankAccountAhorro(Mono<BankAccountDto> bankAccountDto) {
		return bankAccountDto.flatMap(e -> {

			if (e.getCustomer().getType().equalsIgnoreCase(Constantes.CUSTOMER_PERSONAL)) {

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
					Mono<CustomerPersonal> cus = webClientBuilder.build().post()
							.uri(ENDPOINT_SAVE_CUSTOMER_PERSONAL)
							.body(Mono.just(customer), CustomerPersonal.class).retrieve()
							.bodyToMono(CustomerPersonal.class);

					return cus;
				});

			}
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		});
	}

//
	public Mono<?> saveBankAccountCorriente(Mono<BankAccountDto> bankAccountDto) {
		return bankAccountDto.flatMap(e -> {
			if (e.getCustomer().getType().equalsIgnoreCase(Constantes.CUSTOMER_PERSONAL)) {

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
					Mono<CustomerPersonal> cus = webClientBuilder.build().post()
							.uri(ENDPOINT_SAVE_CUSTOMER_PERSONAL)
							.body(Mono.just(customer), CustomerPersonal.class).retrieve()
							.bodyToMono(CustomerPersonal.class);

					return cus;
				});

			} else if (e.getCustomer().getType().equalsIgnoreCase(Constantes.CUSTOMER_EMPRESARIAL)) {

				CustomerDto customerDto = e.getCustomer();
				/*
				 * List<BankAccountCorriente> accountCorriente =
				 * e.getBankAccountCorriente().stream().map(ac -> {
				 * 
				 * ac.setMovements(new ArrayList<>()); return ac;
				 * }).collect(Collectors.toList());
				 */

				e.getBankAccountCorriente().forEach(ac -> ac.setMovements(new ArrayList<>()));

				CustomerBusiness customer = new CustomerBusiness();
				customer.setFirstName(customerDto.getFirstName());
				customer.setLastName(customerDto.getLastName());
				customer.setDni(customerDto.getDni());
				customer.setPhoneNumber(customerDto.getPhoneNumber());

				return bankCorrienteRepository.insert(e.getBankAccountCorriente()).collectList()
						.flatMap(bankAccounts -> {
							customer.setBankAccountCorriente(
									bankAccounts.stream().map(ac -> ac.getId()).collect(Collectors.toList()));
							Mono<CustomerBusiness> cus = webClientBuilder.build().post()
									.uri(ENDPOINT_SAVE_CUSTOMER_BUSINESS)
									.body(Mono.just(customer), CustomerBusiness.class).retrieve()
									.bodyToMono(CustomerBusiness.class);

							return cus;
						});
			}

			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		});
	}

	public Mono<CustomerPersonal> saveBankAccountPlazoFijo(Mono<BankAccountDto> bankAccountDto) {
		return bankAccountDto.flatMap(e -> {

			if (e.getCustomer().getType().equalsIgnoreCase(Constantes.CUSTOMER_PERSONAL)) {

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
					Mono<CustomerPersonal> cus = webClientBuilder.build().post()
							.uri(ENDPOINT_SAVE_CUSTOMER_PERSONAL)
							.body(Mono.just(customer), CustomerPersonal.class).retrieve()
							.bodyToMono(CustomerPersonal.class);

					return cus;
				});

			}
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		});
	}

	public Mono<BankAccountAhorro> doOperationAhorro(Mono<OperationDto> operationDto) {
		return operationDto.flatMap(e -> {

			if (e.getType().equalsIgnoreCase(Constantes.MOVEMENT_DEPOSIT)) {
				Movement movement = new Movement();
				movement.setMovementDate(LocalDateTime.now().toString());
				movement.setType(e.getType());
				movement.setAmount(e.getAmount());
				Mono<Movement> mov = webClientBuilder.build().post().uri(ENDPOINT_SAVE_MOVEMENT)
						.body(Mono.just(movement), Movement.class).retrieve().bodyToMono(Movement.class);
				return mov.flatMap(m -> {
					return bankAhorroRepository.findByPan(e.getPan()).flatMap(bankAccount -> {
						List<String> movs = bankAccount.getMovements();
						movs.add(m.getId());
						bankAccount.setMovements(movs);
						bankAccount.setSaldo(bankAccount.getSaldo() + e.getAmount());
						return bankAhorroRepository.save(bankAccount);
					});
				});

			} else if (e.getType().equalsIgnoreCase(Constantes.MOVEMENT_RETIRE)) {

				Movement movement = new Movement();
				movement.setMovementDate(LocalDateTime.now().toString());
				movement.setType(e.getType());
				movement.setAmount(e.getAmount());

				return bankAhorroRepository.findByPan(e.getPan()).flatMap(bankAccount -> {
					if (bankAccount.getSaldo() - e.getAmount() >= 0) {

						Mono<Movement> mov = webClientBuilder.build().post().uri(ENDPOINT_SAVE_MOVEMENT)
								.body(Mono.just(movement), Movement.class).retrieve().bodyToMono(Movement.class);

						return mov.flatMap(m -> {

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

			if (e.getType().equalsIgnoreCase(Constantes.MOVEMENT_DEPOSIT)) {
				Movement movement = new Movement();
				movement.setMovementDate(LocalDateTime.now().toString());
				movement.setType(e.getType());
				movement.setAmount(e.getAmount());

				Mono<Movement> mov = webClientBuilder.build().post().uri(ENDPOINT_SAVE_MOVEMENT)
						.body(Mono.just(movement), Movement.class).retrieve().bodyToMono(Movement.class);

				return mov.flatMap(m -> {
					return bankCorrienteRepository.findByPan(e.getPan()).flatMap(bankAccount -> {
						List<String> movs = bankAccount.getMovements();
						movs.add(m.getId());
						bankAccount.setMovements(movs);
						bankAccount.setSaldo(bankAccount.getSaldo() + e.getAmount());
						return bankCorrienteRepository.save(bankAccount);
					});
				});

			} else if (e.getType().equalsIgnoreCase(Constantes.MOVEMENT_RETIRE)) {

				Movement movement = new Movement();
				movement.setMovementDate(LocalDateTime.now().toString());
				movement.setType(e.getType());
				movement.setAmount(e.getAmount());
				return bankCorrienteRepository.findByPan(e.getPan()).flatMap(bankAccount -> {
					if (bankAccount.getSaldo() - e.getAmount() >= 0) {

						Mono<Movement> mov = webClientBuilder.build().post().uri(ENDPOINT_SAVE_MOVEMENT)
								.body(Mono.just(movement), Movement.class).retrieve().bodyToMono(Movement.class);

						return mov.flatMap(m -> {

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

			if (e.getType().equalsIgnoreCase(Constantes.MOVEMENT_DEPOSIT)) {
				Movement movement = new Movement();
				movement.setMovementDate(LocalDateTime.now().toString());
				movement.setType(e.getType());
				movement.setAmount(e.getAmount());

				Mono<Movement> mov = webClientBuilder.build().post().uri(ENDPOINT_SAVE_MOVEMENT)
						.body(Mono.just(movement), Movement.class).retrieve().bodyToMono(Movement.class);

				return mov.flatMap(m -> {
					return bankPlazoFijoRepository.findByPan(e.getPan()).flatMap(bankAccount -> {
						List<String> movs = bankAccount.getMovements();
						movs.add(m.getId());
						bankAccount.setMovements(movs);
						bankAccount.setSaldo(bankAccount.getSaldo() + e.getAmount());
						return bankPlazoFijoRepository.save(bankAccount);
					});
				});

			} else if (e.getType().equalsIgnoreCase(Constantes.MOVEMENT_RETIRE)) {

				Movement movement = new Movement();
				movement.setMovementDate(LocalDateTime.now().toString());
				movement.setType(e.getType());
				movement.setAmount(e.getAmount());
				return bankPlazoFijoRepository.findByPan(e.getPan()).flatMap(bankAccount -> {
					if (bankAccount.getSaldo() - e.getAmount() >= 0) {

						Mono<Movement> mov = webClientBuilder.build().post().uri(ENDPOINT_SAVE_MOVEMENT)
								.body(Mono.just(movement), Movement.class).retrieve().bodyToMono(Movement.class);

						return mov.flatMap(m -> {

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
