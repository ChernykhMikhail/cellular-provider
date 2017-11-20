package dev.chernykh.api.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{userId}")
    public ResponseEntity getUser(@PathVariable("userId") Long id) {
        return userService.getById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @GetMapping("/by-tariff/{tariffId}")
    public List<User> getUsers(@PathVariable("tariffId") Long id) {
        return userService.getAllByTariffId(id);
    }

    @PostMapping
    public ResponseEntity saveUser(@RequestBody User user) {
        userService.saveUser(user);
        return ResponseEntity.ok().build();
    }

    @PutMapping
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
