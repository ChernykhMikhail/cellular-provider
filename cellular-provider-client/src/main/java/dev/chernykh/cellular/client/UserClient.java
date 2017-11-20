package dev.chernykh.cellular.client;

import dev.chernykh.cellular.api.user.User;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class UserClient implements UserOperation {

    private final RestTemplate restTemplate;

    private String baseUrl = "http://localhost:8090";

    public UserClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder
                .rootUri(baseUrl)
                .build();
    }

    @Override
    public List<User> getUsers() {

        ParameterizedTypeReference<List<User>> usersRef = new ParameterizedTypeReference<List<User>>() {
        };
        ResponseEntity<List<User>> exchange = restTemplate.exchange("/users", HttpMethod.GET, null, usersRef);
        return exchange.getBody();
    }
}