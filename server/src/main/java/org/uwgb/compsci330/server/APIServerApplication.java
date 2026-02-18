package org.uwgb.compsci330.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class APIServerApplication {
	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
	public static void main(String[] args)
	{
		SpringApplication.run(APIServerApplication.class, args);
	}


}
