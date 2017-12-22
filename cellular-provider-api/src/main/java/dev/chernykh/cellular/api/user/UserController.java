package dev.chernykh.cellular.api.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

/**
 * The rest controller to manage user resources.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Return all existing users.
     *
     * @return the list from users
     */
    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    /**
     * Get a tariff id and returns the list from users.
     *
     * @param tariffId the tariff id
     * @return the list from found users
     */
    @GetMapping(params = "tariffId")
    public List<User> getUsers(@RequestParam long tariffId) {
        return userService.getUsersByTariffId(tariffId);
    }

    /**
     * Get a user id and returns the user if exists.
     *
     * @param id the user's id
     * @return the found user
     * @throws {@link UserNotFoundException} if user doesn't exist
     */
    @GetMapping("/{userId}")
    public ResponseEntity getUser(@PathVariable("userId") Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    /**
     * Get a user details to save as the new one.
     *
     * @param user the user without id
     * @return {@code HttpStatus.OK}
     */
    @PostMapping(consumes = APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity saveUser(User user) {
        User savedUser = userService.saveUser(user);
        return ResponseEntity
                .created(UriComponentsBuilder.newInstance()
                        .scheme("http")
                        .host("localhost")
                        .port(8090)
                        .path("/users/" + savedUser.getId())
                        .build().toUri())
                .body(savedUser);
    }

    /**
     * Get a user id to delete user with this id.
     *
     * @param id the user id
     * @return {@code HttpStatus.NO_CONTENT}
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity removeUser(@PathVariable("userId") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
