package Domain;

import java.time.LocalDateTime;

public class Credential {
    private String passwordHash;
    private String salt;
    private LocalDateTime lastChangeAt;

    public Credential(String passwordHash, String salt, LocalDateTime lastChangeAt) {
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.lastChangeAt = lastChangeAt;
    }

    public String getPasswordHash() {return passwordHash;}
    public void setPasswordHash(String passwordHash) {this.passwordHash = passwordHash;}

    public String getSalt() {return salt;}
    public void setSalt(String salt) {this.salt = salt;}

    public LocalDateTime getLastChangeAt() {return lastChangeAt;}
    public void setLastChangeAt(LocalDateTime lastChangeAt) {this.lastChangeAt = lastChangeAt;}
}