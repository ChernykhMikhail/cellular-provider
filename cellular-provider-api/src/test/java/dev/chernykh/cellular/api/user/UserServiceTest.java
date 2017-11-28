package dev.chernykh.cellular.api.user;

import dev.chernykh.cellular.api.RestApiApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RestApiApplication.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private User user;

    @Before
    public void setUp() throws Exception {
        user = new User(1L, "John", "Doe", null, 1L);
    }

    @Test
    public void saveUserTest() {
        when(userRepository.save(user)).thenReturn(user);
        userService.saveUser(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void deleteUserTest() {
        doNothing().when(userRepository).delete(isA(Long.class));
        userService.deleteOne(user.getId());
        verify(userRepository, times(1)).delete(user.getId());
    }
}