package dev.chernykh.cellular.api.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void shouldReturnAllUsers() {
        when(userRepository.findAll()).thenReturn(asList(
                new User(1L, "John Doe", 7L),
                new User(2L, "James Bond", 2L),
                new User(3L, "Taylor Swift", 5L)
        ));

        List<User> users = userService.getUsers();

        assertThat(users, notNullValue());
        assertThat(users, hasSize(3));

        assertThat(users.get(0), notNullValue());
        assertThat(users.get(0).getId(), is(1L));
        assertThat(users.get(0).getFullName(), is("John Doe"));
        assertThat(users.get(0).getTariffId(), is(7L));

        assertThat(users.get(1), notNullValue());
        assertThat(users.get(1).getId(), is(2L));
        assertThat(users.get(1).getFullName(), is("James Bond"));
        assertThat(users.get(1).getTariffId(), is(2L));

        assertThat(users.get(2), notNullValue());
        assertThat(users.get(2).getId(), is(3L));
        assertThat(users.get(2).getFullName(), is("Taylor Swift"));
        assertThat(users.get(2).getTariffId(), is(5L));

        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void shouldGetUserById() {
        when(userRepository.findOne(7L))
                .thenReturn(new User(7L, "Patrick Jane", 9L));

        Optional<User> foundUser = userService.getUserById(7L);

        assertThat(foundUser, notNullValue());
        assertTrue(foundUser.isPresent());
        assertThat(foundUser.get().getId(), is(7L));
        assertThat(foundUser.get().getFullName(), is("Patrick Jane"));
        assertThat(foundUser.get().getTariffId(), is(9L));

        verify(userRepository, times(1)).findOne(7L);
    }

    @Test
    public void shouldGetUsersByTariffId() {
        when(userRepository.findAllByTariffId(2L)).thenReturn(asList(
                new User(1L, "Sarah Connor", 2L),
                new User(2L, "LeBron James", 2L)
        ));

        List<User> users = userService.getUsersByTariffId(2L);

        assertThat(users, notNullValue());
        assertThat(users, hasSize(2));

        assertThat(users.get(0), notNullValue());
        assertThat(users.get(0).getId(), is(1L));
        assertThat(users.get(0).getFullName(), is("Sarah Connor"));
        assertThat(users.get(0).getTariffId(), is(2L));

        assertThat(users.get(1), notNullValue());
        assertThat(users.get(1).getId(), is(2L));
        assertThat(users.get(1).getFullName(), is("LeBron James"));
        assertThat(users.get(1).getTariffId(), is(2L));

        verify(userRepository, times(1)).findAllByTariffId(2L);
    }

    @Test
    public void shouldGetUsersByNameLike() {
        when(userRepository.findAllByFullNameLike("%James%")).thenReturn(asList(
                new User(3L, "James Harden", 5L),
                new User(7L, "James Bond", 7L)
        ));

        List<User> users = userService.getUsersByNameLike("James");

        assertThat(users, notNullValue());
        assertThat(users, hasSize(2));

        assertThat(users.get(0), notNullValue());
        assertThat(users.get(0).getId(), is(3L));
        assertThat(users.get(0).getFullName(), is("James Harden"));
        assertThat(users.get(0).getTariffId(), is(5L));

        assertThat(users.get(1), notNullValue());
        assertThat(users.get(1).getId(), is(7L));
        assertThat(users.get(1).getFullName(), is("James Bond"));
        assertThat(users.get(1).getTariffId(), is(7L));

        verify(userRepository, times(1)).findAllByFullNameLike("%James%");
    }

    @Test
    public void shouldCreateUserAndReturnIt() {
        User user = new User(1L, "John Doe", 1L);
        when(userRepository.save(user))
                .thenReturn(user);

        User savedUser = userService.saveUser(user);

        assertThat(savedUser, notNullValue());
        assertThat(savedUser.getTariffId(), is(1L));
        assertThat(savedUser.getFullName(), is("John Doe"));
        assertThat(savedUser.getTariffId(), is(1L));

        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void shouldDeleteUserByGivenId() {
        doNothing()
                .when(userRepository).delete(3L);

        userService.deleteUser(3L);

        verify(userRepository, times(1)).delete(3L);
    }
}