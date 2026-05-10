package Domain;

import java.util.Optional;
import java.util.UUID;
import java.util.List;


public interface IUserRepository {
    void save(User user);

    Optional<User> findById(UUID id);

    Optional<User> findByUserName(String userName);

    List<User> findAll();
}