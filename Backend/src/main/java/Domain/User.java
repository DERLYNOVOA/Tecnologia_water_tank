package LibraryManager;

import java.util.UUID;

public class User {
    private UUID id;
    private String userName;
    private RoleType role;

    public User(UUID id, String userName, RoleType role) {
        this.id = id;
        this.userName = userName;
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }
}

