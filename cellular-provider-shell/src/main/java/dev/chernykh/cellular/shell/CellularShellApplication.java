package dev.chernykh.cellular.shell;

import dev.chernykh.cellular.client.TariffClient;
import dev.chernykh.cellular.client.UserClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CellularShellApplication {
    public static void main(String[] args) {
        SpringApplication.run(CellularShellApplication.class);
    }

    @Bean
    public UserClient userClient(RestTemplateBuilder restTemplateBuilder) {
        return new UserClient(restTemplateBuilder);
    }

    @Bean
    public TariffClient tariffClient(RestTemplateBuilder restTemplateBuilder) {
        return new TariffClient(restTemplateBuilder);
    }
}
