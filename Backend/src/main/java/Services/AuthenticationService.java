package Services;

import Domain.User;
import Repository.UserRepository;
import java.util.Optional;

public class AuthenticationService {
    private UserRepository userRepository;
    // 1. Declaramos la interfaz (El QUÉ)
    private PasswordHasher passwordHasher;

    // 2. Inyectamos la dependencia en el constructor
    public AuthenticationService(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    public Optional<User> login(String userName, String password) {
        Optional<User> userOpt = userRepository.findByUserName(userName);

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // 3. USAMOS LA VARIABLE (en minúscula), NO LA CLASE
            // Y llamamos al método que definimos en la interfaz
            if (user.isActive() && passwordHasher.verifyPassword(password, user.getCredential().getPasswordHash())) {
                return userOpt;
            }
        }
        return Optional.empty();
    }

    public Optional<User> logout() {
        return Optional.empty();
    }
}
