package com.podolist.podolist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PoDoListApplication {

	public static void main(String[] args) {
		SpringApplication.run(PoDoListApplication.class, args);
	}

}
