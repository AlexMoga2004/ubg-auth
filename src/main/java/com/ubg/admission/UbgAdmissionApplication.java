package com.ubg.admission;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude= {SecurityAutoConfiguration.class})
public class UbgAdmissionApplication {
	public static void main(String[] args) {
		SpringApplication.run(UbgAdmissionApplication.class, args);
	}
}
