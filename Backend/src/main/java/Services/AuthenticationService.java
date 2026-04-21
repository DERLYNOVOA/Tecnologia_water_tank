package Services;

import Domain.User;
import Repository.UserRepository;
import java.util.Optional;

public class AuthenticationService {
    private UserRepository userRepository;
    private PasswordHasher passwordHasher;

    //Inyectamos la dependencia en el constructor
    public AuthenticationService(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    public Optional<User> login(String userName, String password) {
        Optional<User> userOpt = userRepository.findByUserName(userName);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
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
