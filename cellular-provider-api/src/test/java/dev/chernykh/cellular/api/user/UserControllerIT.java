package dev.chernykh.cellular.api.user;

import dev.chernykh.cellular.api.RestApiApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RestApiApplication.class)
@WebAppConfiguration
public class UserControllerIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private User user;
    private List<User> userList;
    private String jsonUser;
    private String jsonUsersArray;

    @Before
    public void setUp() throws Exception {

        mockMvc = webAppContextSetup(webApplicationContext).build();

        user = new User(1L, "James", "Harden", null, 2L);
        userList = Stream.of(
                new User(1L, "James", "Bond", null, 1L),
                new User(2L, "Patrick", "Jane", null, 1L),
                new User(3L, "Sara", "Connor", null, 1L)
        ).collect(toList());
        //language=JSON
        jsonUser = "{\n" +
                "  \"id\": 1,\n" +
                "  \"firstName\": \"James\",\n" +
                "  \"lastName\": \"Harden\",\n" +
                "  \"middleName\": null,\n" +
                "  \"tariffId\": 2\n" +
                "}";
        jsonUsersArray = "[\n" +
                "  {\"id\": 1, \"firstName\": \"James\", \"lastName\": \"Bond\", \"middleName\": null, \"tariffId\": 1},\n" +
                "  {\"id\": 2, \"firstName\": \"Patrick\", \"lastName\": \"Jane\", \"middleName\": null, \"tariffId\": 1},\n" +
                "  {\"id\": 3, \"firstName\": \"Sara\", \"lastName\": \"Connor\", \"middleName\": null, \"tariffId\": 1}\n" +
                "]";
    }

    @Test
    public void getUserAndExpectSuccess() throws Exception {

        given(userService.getById(user.getId()))
                .willReturn(Optional.of(user));
        mockMvc.perform(get("/users/" + user.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonUser));
    }

    @Test
    public void getAllUsersAndExpectSuccess() throws Exception {

        given(userService.getUsers()).willReturn(userList);

        mockMvc.perform(get("/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonUsersArray))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(userList.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].firstName", is(userList.get(0).getFirstName())))
                .andExpect(jsonPath("$[0].lastName", is(userList.get(0).getLastName())))
                .andExpect(jsonPath("$[0].tariffId", is(userList.get(0).getTariffId().intValue())))

                .andExpect(jsonPath("$[1].id", is(userList.get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].firstName", is(userList.get(1).getFirstName())))
                .andExpect(jsonPath("$[1].lastName", is(userList.get(1).getLastName())))
                .andExpect(jsonPath("$[1].tariffId", is(userList.get(1).getTariffId().intValue())))

                .andExpect(jsonPath("$[2].id", is(userList.get(2).getId().intValue())))
                .andExpect(jsonPath("$[2].firstName", is(userList.get(2).getFirstName())))
                .andExpect(jsonPath("$[2].lastName", is(userList.get(2).getLastName())))
                .andExpect(jsonPath("$[2].tariffId", is(userList.get(2).getTariffId().intValue())));
    }

    @Test
    public void getWrongUserAndExpectFailure() throws Exception {
        given(userService.getById(user.getId())).willThrow(UserNotFoundException.class);
        mockMvc.perform(get("/users/" + user.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addUserAndExpectSuccess() throws Exception {

        mockMvc.perform(put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUser))
                .andExpect(status().isCreated());
    }

    @Test
    public void updateUserAndExpectSuccess() throws Exception {

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUser))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteUserAndExpectSuccess() throws Exception {
        mockMvc.perform(delete("/users/" + user.getId()))
                .andExpect(status().isNoContent());
    }
}