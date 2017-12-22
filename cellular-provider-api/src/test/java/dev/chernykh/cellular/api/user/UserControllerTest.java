package dev.chernykh.cellular.api.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void shouldReturnUserWithGivenId() throws Exception {
        given(userService.getUserById(5L))
                .willReturn(Optional.of(new User(5L, "Cris Paul", 5L)));

        mockMvc.perform(
                get("/users/5")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.fullName", is("Cris Paul")))
                .andExpect(jsonPath("$.id", is(5)));

        verify(userService, times(1)).getUserById(5L);
    }

    @Test
    public void shouldReturnErrorIfUserWithGivenIdNotFound() throws Exception {
        given(userService.getUserById(2L))
                .willReturn(Optional.empty());

        mockMvc.perform(
                get("/users/2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUserById(2L);
    }

    @Test
    public void shouldReturnUsersByTariffId() throws Exception {
        given(userService.getUsersByTariffId(4L))
                .willReturn(asList(
                        new User(11L, "Peter Falk", 4L),
                        new User(15L, "Frank Sinatra", 4L)
                ));

        mockMvc.perform(
                get("/users")
                        .param("tariffId", "4")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(11)))
                .andExpect(jsonPath("$[0].fullName", is("Peter Falk")))
                .andExpect(jsonPath("$[0].tariffId", is(4)))
                .andExpect(jsonPath("$[1].id", is(15)))
                .andExpect(jsonPath("$[1].fullName", is("Frank Sinatra")))
                .andExpect(jsonPath("$[1].tariffId", is(4)));


        verify(userService, times(1)).getUsersByTariffId(4L);
    }

    @Test
    public void shouldReturnListOfUsers() throws Exception {
        given(userService.getUsers()).willReturn(asList(
                new User(1L, "Josh Long", 3L),
                new User(2L, "Steve Jobs", 4L),
                new User(5L, "Dean Martin", 2L)
        ));

        mockMvc.perform(
                get("/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].fullName", is("Josh Long")))
                .andExpect(jsonPath("$[0].tariffId", is(3)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].fullName", is("Steve Jobs")))
                .andExpect(jsonPath("$[1].tariffId", is(4)))
                .andExpect(jsonPath("$[2].id", is(5)))
                .andExpect(jsonPath("$[2].fullName", is("Dean Martin")))
                .andExpect(jsonPath("$[2].tariffId", is(2)));

        verify(userService, times(1)).getUsers();
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void shouldCreateUserAndReturnIt() throws Exception {
        User user = new User(2L, "John Doe", 2L);
        given(userService.saveUser(user))
                .willReturn(user);

        mockMvc.perform(
                post("/users")
                        .param("id", "2")
                        .param("fullName", "John Doe")
                        .param("tariffId", "2")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", "http://localhost:8090/users/2"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.fullName", is("John Doe")))
                .andExpect(jsonPath("$.tariffId", is(2)));

        verify(userService, atLeastOnce()).saveUser(user);
    }

    @Test
    public void shouldDeleteUserById() throws Exception {
        doNothing()
                .when(userService).deleteUser(9L);

        mockMvc.perform(
                delete("/users/9"))
                .andExpect(status().isNoContent());

        verify(userService, atLeastOnce()).deleteUser(9L);
    }
}