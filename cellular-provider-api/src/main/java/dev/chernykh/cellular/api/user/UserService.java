package dev.chernykh.cellular.api.user;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public List<User> getUsers() {
        return repository.findAll();
    }

    public Optional<User> getById(@NonNull Long id) {
        return Optional.ofNullable(repository.findOne(id));
    }

    public List<User> getAllByTariffId(@NonNull Long id) {
        return repository.findAllByTariffId(id);
    }

    public void saveUser(@NonNull User user) {
        repository.save(user);
    }

    public void deleteOne(@NonNull Long id) {
        repository.delete(id);
    }
}
