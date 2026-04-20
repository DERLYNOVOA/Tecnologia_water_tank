package Repository;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Implementación de IRepositoryLog que persiste en archivo de texto plano.
 * Guarda logs en historial_tanque.txt con formato: [fecha hora] mensaje
 */
public class FileLogRepository implements IRepositoryLog {

    private static final String LOG_FILE = "historial_tanque.txt";
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void saveLog(String message) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            String timestamp = LocalDateTime.now().format(FORMATTER);
            String logEntry = String.format("[%s] %s", timestamp, message);
            writer.println(logEntry);
        } catch (IOException e) {
            System.err.println("Error al guardar log: " + e.getMessage());
        }
    }
}