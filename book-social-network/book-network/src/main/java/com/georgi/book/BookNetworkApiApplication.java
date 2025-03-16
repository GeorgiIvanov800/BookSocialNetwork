package com.georgi.book;

import com.georgi.book.role.Role;
import com.georgi.book.role.RoleRepository;
import com.georgi.book.user.User;
import com.georgi.book.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableAsync
public class BookNetworkApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookNetworkApiApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {

			if (roleRepository.findByName("USER").isEmpty()) {
				roleRepository.save(
						Role.builder().name("USER").build()
				);

			}

			var userRole = roleRepository.findByName("USER")
					//todo better exception handling
					.orElseThrow(() -> new RuntimeException("User role not found"));

			if(userRepository.count() == 0) {
				userRepository.save(
						User.builder()
								.email("test@gmail.com")
								.password(passwordEncoder.encode("test1234test"))
								.enabled(true)
								.accountLocked(false)
								.firstName("John")
								.lastName("Doe")
								.roles(List.of(userRole))
								.build()
				);
			}


		};
	}
}
