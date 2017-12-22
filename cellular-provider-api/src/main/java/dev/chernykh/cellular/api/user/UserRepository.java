package dev.chernykh.cellular.api.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByTariffId(Long id);
    List<User> findAllByFullNameLike(String name);
    List<User> findAllByFullNameLikeAndTariffId(String name, long tariffId);
}
