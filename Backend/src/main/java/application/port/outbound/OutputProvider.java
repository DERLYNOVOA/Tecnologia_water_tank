package application.port.outbound;

public interface OutputProvider {
    void print(String message);
    void println(String message);
    void printf(String format, Object... args);
}
