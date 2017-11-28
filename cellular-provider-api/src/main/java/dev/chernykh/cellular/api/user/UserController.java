package dev.chernykh.cellular.api.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping(params = "tariffId")
    public List<User> getUsers(@RequestParam long tariffId) {
        return userService.getAllByTariffId(tariffId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity getUser(@PathVariable("userId") Long id) {
        return userService.getById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new UserNotFoundException(id));
    }


    @PostMapping(consumes = APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity saveUser(@RequestBody User user) {
        userService.saveUser(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/{id}", consumes = APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity updateUser(@RequestBody User user) {
        userService.saveUser(user);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity removeUser(@PathVariable("userId") Long id) {
        userService.deleteOne(id);
        return ResponseEntity.noContent().build();
    }
}
