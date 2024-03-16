package com.sheywesk.aop.example.logs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class LogsExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(LogsExampleApplication.class, args);
	}

}
