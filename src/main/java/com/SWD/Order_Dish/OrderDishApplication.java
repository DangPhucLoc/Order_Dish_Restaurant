package com.SWD.Order_Dish;

import com.SWD.Order_Dish.entity.AccountEntity;
import com.SWD.Order_Dish.enums.Role;
import com.SWD.Order_Dish.repository.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import java.time.LocalDateTime;
import java.util.Date;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
public class OrderDishApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderDishApplication.class, args);
	}
	@Bean
	public CommandLineRunner run(AccountRepository accountRepository) {
		return args -> {
			if (accountRepository.findByEmail("admin@gmail.com").isEmpty()) {
				AccountEntity account = new AccountEntity();
				account.setIsUnlocked(true);
				account.setEmail("admin@gmail.com");
				account.setPassword("admin");
				account.setRole(Role.MANAGER);
				account.setIsAvailable(true);
				account.setAddress("sada");
				account.setBirthday(LocalDateTime.now());
				account.setFullName("admin");
				account.setPhoneNumber("123456789");
				account.setCreatedBy("admin");
				account.setUpdatedDate(new Date());
				account.setCreatedDate(new Date());
				account.setIsEnable(true);
				account.setModifiedBy("admin");
				accountRepository.save(account);

			}
		};
	}
}
