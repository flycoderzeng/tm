package com.tm.worker;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableFeignClients
@EnableTransactionManagement
@EnableAsync
@SpringBootApplication(exclude= DataSourceAutoConfiguration.class, scanBasePackages = {"com.tm.worker", "com.tm.common"})
@MapperScan({"com.tm.common"})
@EnableDiscoveryClient
public class WorkerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkerApplication.class, args);
    }

}
