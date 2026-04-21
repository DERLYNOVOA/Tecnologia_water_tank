package Domain;

import java.util.UUID;

public class User {
    private UUID id;
    private String userName;
    private RoleType role;
    private boolean isActive;
    private Credential credential; //___?????

    public User(UUID id, String userName, RoleType role, boolean isActive, Credential credential) {
        this.id = id;
        this.userName = userName;
        this.role = role;
        this.isActive = isActive;
        this.credential = credential;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public RoleType getRole() { return role; }
    public void setRole(RoleType role) { this.role = role; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public Credential getCredential() { return credential; }
    public void setCredential(Credential credential) { this.credential = credential; }
}

