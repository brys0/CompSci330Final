package org.uwgb.compsci330.server;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
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
	@SuppressWarnings("unused")
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI().info(new Info()
				.title("Chat Server API")
				.version(Configuration.SERVER_VERSION)
				.description("Generated documentation for the chat server api"));
	}


	static void main(String[] args)
	{
		SpringApplication.run(APIServerApplication.class, args);
	}
}
