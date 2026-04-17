package Services;

public class SimplePasswordHasher {
    public String hash(String password, String salt) {
        // Implementación simple de hash (solo para fines ilustrativos)
        return password + ":" + salt;
    }

    public boolean verify(String plain, String salt, String expected) {
        return hash(plain, salt).equals(expected);
    }
}

