package dev.chernykh.cellular.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link UserOperations} interface using Spring's {@link RestTemplate}.
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
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("fullName", name);
        map.add("tariffId", String.valueOf(tariffId));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        return restTemplate.postForEntity("/users", request, UserDto.class).getBody();
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
        String url = "/users/" + id;
        restTemplate.delete(url);
    }

    @Override
    public UserDto update(long id, String name, long tariffId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("id", String.valueOf(id));
        map.add("fullName", name);
        map.add("tariffId", String.valueOf(tariffId));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        return restTemplate.postForEntity("/users", request, UserDto.class).getBody();
    }
}
