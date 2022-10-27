package com.tm.lc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan
public class LcApplication {

	public static void main(String[] args) {
		SpringApplication.run(LcApplication.class, args);
	}

}
