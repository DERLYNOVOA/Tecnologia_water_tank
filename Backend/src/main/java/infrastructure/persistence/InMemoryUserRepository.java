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

        // 1. Creamos las credenciales primero (Fíjate que le pasamos un salt vacío "" por ahora)
        Credential credAdmin = new Credential(hasher.hashPassword("123"), "", LocalDateTime.now());
        Credential credUser = new Credential(hasher.hashPassword("123"), "", LocalDateTime.now());

        // 2. Creamos los usuarios pasándoles TODOS los parámetros de tu clase
        User admin = new User(
                UUID.randomUUID(), // Genera un ID único automático
                "ADMIN",           // userName
                RoleType.ADMIN,    // role
                true,              // isActive
                credAdmin          // credential
        );

        User normalUser = new User(
                UUID.randomUUID(),
                "USER",
                RoleType.USER,
                true,
                credUser
        );

        // 3. Los guardamos en el mapa simulando la base de datos
        save(admin);
        save(normalUser);
    }

    @Override
    public void save(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public Optional<User> findByUserName(String userName) {
        // CORRECCIÓN CLAVE: Buscamos el nombre directamente en 'u' (User), ya no en Credential
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