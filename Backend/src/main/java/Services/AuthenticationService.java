package Services;

import Domain.IUserRepository;
import Domain.User;
import java.util.Optional;

public class AuthenticationService {
    private IUserRepository userRepository;
    private PasswordHasher passwordHasher;

    //Inyectamos la dependencia en el constructor
    public AuthenticationService(IUserRepository userRepository, PasswordHasher passwordHasher) {
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

}
