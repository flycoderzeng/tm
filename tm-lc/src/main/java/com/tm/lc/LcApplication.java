package com.tm.lc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@EntityScan
@SpringBootApplication(exclude= DataSourceAutoConfiguration.class, scanBasePackages = {"com.tm.web", "com.tm.common"})
@MapperScan({"com.tm.common"})
public class LcApplication {

	public static void main(String[] args) {
		SpringApplication.run(LcApplication.class, args);
	}

}
