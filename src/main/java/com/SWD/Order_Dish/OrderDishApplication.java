package com.SWD.Order_Dish;

import com.SWD.Order_Dish.enums.Role;
import com.SWD.Order_Dish.model.authentication.RegisterRequest;
import com.SWD.Order_Dish.repository.UserRepository;
import com.SWD.Order_Dish.service.AuthenticationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
public class OrderDishApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderDishApplication.class, args);
	}
	@Bean
	public CommandLineRunner run(AuthenticationService authenticationService, UserRepository accountRepository) {
		return args -> {
			if (accountRepository.findByEmail("admin@gmail.com").isEmpty()) {
				var admin = RegisterRequest.builder()
						.displayName("Admin")
						.email("admin@gmail.com")
						.password("admin")
						.role(Role.MANAGER)
						.address("VLA")
						.phoneNumber("0903159357")
						.birthday("2003-10-20")
						.build();
				authenticationService.register(admin);
			}
		};
	}
}
