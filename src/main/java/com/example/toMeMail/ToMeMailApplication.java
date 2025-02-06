package com.example.toMeMail;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class ToMeMailApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(ToMeMailApplication.class, args);
		//System.out.println(Arrays.toString(context.getBeanDefinitionNames()));
	}

	@Bean
	CommandLineRunner printDatabaseUrl(DatabaseConfig databaseConfig) {
		return args -> {
			System.out.println("🔍 DATABASE URL: " + databaseConfig.getDatabaseUrl());
		};
	}

}
