package com.quostomize.quostomize_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class QuostomizeBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuostomizeBeApplication.class, args);
	}

}
