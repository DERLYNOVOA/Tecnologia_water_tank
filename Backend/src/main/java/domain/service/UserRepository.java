package domain.service;

import domain.model.User;

import java.util.Optional;
import java.util.UUID;
import java.util.List;


public interface UserRepository {
    void save(User user);

    Optional<User> findById(UUID id);

    Optional<User> findByUserName(String userName);

    List<User> findAll();
}