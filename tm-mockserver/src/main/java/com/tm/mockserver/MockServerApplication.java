package com.tm.mockserver;

import com.tm.mockserver.netty.mock.MockNettyServer;
import jakarta.inject.Inject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableScheduling
@EnableTransactionManagement
@EnableAsync
@SpringBootApplication(exclude= DataSourceAutoConfiguration.class, scanBasePackages = {"com.tm.mockserver", "com.tm.common"})
@MapperScan({"com.tm.common"})
@EnableDiscoveryClient
public class MockServerApplication implements CommandLineRunner {

    private final MockNettyServer nettyServer;

    @Inject
    public MockServerApplication(MockNettyServer nettyServer) {
        this.nettyServer = nettyServer;
    }

    public static void main(String[] args) {
        SpringApplication.run(MockServerApplication.class, args);
    }

    @Override
    public void run(String... args) {
        nettyServer.start();
    }
}
