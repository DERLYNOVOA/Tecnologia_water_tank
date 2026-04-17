package Services;

import Domain.User;
import java.util.Optional;

public class Authenticator {
    private AuthenticationService authenticationService;
    private AuthorizationService authorizationService;
    private User currentUser;

    public Authenticator(AuthenticationService authenticationService, AuthorizationService authorizationService) {
        this.authenticationService = authenticationService;
        this.authorizationService = authorizationService;
        this.currentUser = null;
    }

    public boolean login(String userName, String password) {
        Optional<User> userOpt = authenticationService.login(userName, password);
        if (userOpt.isPresent()) {
            currentUser = userOpt.get();
            return true;
        }
        return false;
    }

    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}

