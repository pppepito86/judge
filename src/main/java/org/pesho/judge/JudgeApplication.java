package org.pesho.judge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class JudgeApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(JudgeApplication.class, args);
	}
	
	
	
}
