package Repository;

import Domain.User;

import java.util.Optional;

public interface UserRepository {
    void save(User user);
    Optional<User> findByUserName(String userName);
    Optional<User> findById(java.util.UUID id);
}

