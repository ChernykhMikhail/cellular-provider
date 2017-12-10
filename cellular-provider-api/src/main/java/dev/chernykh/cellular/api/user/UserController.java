package dev.chernykh.cellular.api.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

/**
 * The rest controller to manage users.
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
     * Retrieves all existing users.
     *
     * @return the list of users
     */
    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    /**
     * Retrieves the list of users with given tariff id.
     *
     * @param tariffId the tariff id
     * @return the list of found users
     */
    @GetMapping(params = "tariffId")
    public List<User> getUsers(@RequestParam long tariffId) {
        return userService.getAllByTariffId(tariffId);
    }

    /**
     * Retrieves user by gotten id.
     *
     * @param id the user's id
     * @return the http status OK
     * @throws {@code UserNotFoundException} if user doesn't exist
     */
    @GetMapping("/{userId}")
    public ResponseEntity getUser(@PathVariable("userId") Long id) {
        return userService.getById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    /**
     * Saves the gotten user.
     *
     * @param user the user
     * @return the http status OK
     */
    @PostMapping(consumes = APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity saveUser(@RequestBody User user) {
        userService.saveUser(user);
        return ResponseEntity.ok().build();
    }

    /**
     * Updates the gotten user.
     *
     * @param user the user
     * @return the http status CREATED
     */
    @PostMapping(path = "/{id}", consumes = APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity updateUser(@RequestBody User user) {
        userService.saveUser(user);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     * Deletes a user by the gotten id
     *
     * @param id an id
     * @return the http status NO_CONTENT
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity removeUser(@PathVariable("userId") Long id) {
        userService.deleteOne(id);
        return ResponseEntity.noContent().build();
    }
}
