package com.CustomerRelationshipManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CustomerRelationshipManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerRelationshipManagementApplication.class, args);
	}

}
