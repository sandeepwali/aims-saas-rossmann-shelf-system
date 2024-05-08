package com.solumesl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication(exclude={SecurityAutoConfiguration.class,
		UserDetailsServiceAutoConfiguration.class
})
@Configuration
@ComponentScan({"com.solumesl"})
public class ShelfSystem {

	public static void main(String[] args) {
		 
		SpringApplication.run(ShelfSystem.class, args);
	}

}
