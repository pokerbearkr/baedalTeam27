package org.example.baedalteam27;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BaedalTeam27Application {

	public static void main(String[] args) {
		SpringApplication.run(BaedalTeam27Application.class, args);
	}

}
