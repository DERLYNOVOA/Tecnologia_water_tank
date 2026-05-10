package application.service;

import domain.event.RoleType;
import domain.model.User;

public class AuthorizationService {
    
    public boolean hasPermission(User user, String code) {
        if (user.getRole() == RoleType.ADMIN) {
            return true;
        }
        return false;
    }
}

