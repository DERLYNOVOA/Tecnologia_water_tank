package application.service;

import domain.event.RoleType;
import domain.model.User;

public class AuthorizationService {
    public boolean hasPermission(User user, String code) {
        return user.getRole() == RoleType.ADMIN;
    }
}

