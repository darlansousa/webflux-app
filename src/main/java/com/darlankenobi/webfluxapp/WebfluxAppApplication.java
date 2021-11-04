package com.darlankenobi.webfluxapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.blockhound.BlockHound;

@SpringBootApplication
public class WebfluxAppApplication {
	public static void main(String[] args) {
		SpringApplication.run(WebfluxAppApplication.class, args);
	}

}
