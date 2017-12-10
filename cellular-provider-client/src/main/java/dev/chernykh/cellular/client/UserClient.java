package dev.chernykh.cellular.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 *
 */
public class UserClient implements UserOperations {

    private RestTemplate restTemplate;

    @Autowired
    public UserClient(RestTemplateBuilder templateBuilder) {
        this.restTemplate = templateBuilder
                .rootUri("http://localhost:8090")
                .build();
    }

    @Override
    public UserDto add(String name, long tariffId) {
        return restTemplate.getForObject("/users", UserDto.class);
    }

    @Override
    public List<UserDto> getAll(String name, long tariffId) {
        ParameterizedTypeReference<List<UserDto>> ref = new ParameterizedTypeReference<List<UserDto>>() {
        };
        ResponseEntity<List<UserDto>> responseEntity = restTemplate.exchange("/users", HttpMethod.GET, null, ref);
        return responseEntity.getBody();
    }

    @Override
    public void delete(long id) {

    }

    @Override
    public UserDto update(long id, String name, long tariffId) {
        return null;
    }
}
