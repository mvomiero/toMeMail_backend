package com.example.toMeMail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

@SpringBootApplication
public class ToMeMailApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(ToMeMailApplication.class, args);
		System.out.println(Arrays.toString(context.getBeanDefinitionNames()));
	}

}
