package dev.chernykh.cellular.api.user;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> getUsers() {
        return repository.findAll();
    }

    public List<User> getUsersByNameLike(@NotNull @NotBlank String likeName) {
        return repository.findAllByFullNameLike("%" + likeName + "%");
    }

    public Optional<User> getUserById(@NotNull Long id) {
        return Optional.ofNullable(repository.findOne(id));
    }

    public List<User> getUsersByTariffId(@NotNull Long id) {
        return repository.findAllByTariffId(id);
    }

    public User saveUser(@NotNull User user) {
        return repository.save(user);
    }

    public void deleteUser(@NotNull Long id) {
        repository.delete(id);
    }
}
