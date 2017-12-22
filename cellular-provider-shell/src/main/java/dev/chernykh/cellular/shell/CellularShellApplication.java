package dev.chernykh.cellular.shell;

import dev.chernykh.cellular.client.TariffClient;
import dev.chernykh.cellular.client.UserClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;

//@EnableConfigurationProperties(UserClient.class)
@SpringBootApplication
public class CellularShellApplication {

    @Bean
    FormattingConversionService conversionService() {
        return new DefaultFormattingConversionService();
    }

    @Bean
    public UserClient userClient(RestTemplateBuilder restTemplateBuilder) {
        return new UserClient(restTemplateBuilder);
    }

    @Bean
    public TariffClient tariffClient(RestTemplateBuilder restTemplateBuilder) {
        return new TariffClient(restTemplateBuilder);
    }

    public static void main(String[] args) {
        SpringApplication.run(CellularShellApplication.class);
    }
}
