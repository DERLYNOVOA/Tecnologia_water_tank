package repository;

import Domain.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class nMemoryUserRepository implements UserRepository {
    private Map<UUID, User> users = new HashMap<>();

    @Override
    public void save(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public Optional<User> findByUserName(String userName) {
        return users.values().stream()
                .filter(u -> u.getusername().equals(userName))
                .findFirst();
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(users.get(id));
    }
}

