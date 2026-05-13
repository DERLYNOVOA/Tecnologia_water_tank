package infrastructure.persistence;

import domain.model.User;
import domain.service.UserRepository;
import domain.model.Credential;
import domain.event.RoleType;
import infrastructure.security.SimplePasswordHasher;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryUserRepository implements UserRepository {
    private Map<UUID, User> users = new HashMap<>();

    public InMemoryUserRepository() {
        SimplePasswordHasher hasher = new SimplePasswordHasher();

        Credential credAdmin = new Credential(hasher.hashPassword("123"), "", LocalDateTime.now());
        Credential credUser = new Credential(hasher.hashPassword("123"), "", LocalDateTime.now());

        User admin = new User(
                UUID.randomUUID(),
                "ADMIN",
                RoleType.ADMIN,
                true,
                credAdmin
        );

        User normalUser = new User(
                UUID.randomUUID(),
                "USER",
                RoleType.USER,
                true,
                credUser
        );

        save(admin);
        save(normalUser);
    }

    @Override
    public void save(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public Optional<User> findByUserName(String userName) {

        return users.values().stream()
                .filter(u -> u.getUserName().equalsIgnoreCase(userName))
                .findFirst();
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> findAll() {
     return new ArrayList<>(users.values());
}
}