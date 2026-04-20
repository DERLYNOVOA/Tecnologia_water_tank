package Services;

import Domain.RoleType;
import Domain.User;

public class AuthorizationService {
    
    public boolean hasPermission(User user, String action) {
        if (user.getRole() == RoleType.ADMIN) {
            return true;
        }
        return false;
    }
}

