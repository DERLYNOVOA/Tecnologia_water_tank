package infrastructure.persistence;

import domain.service.RepositoryLog;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FileLogRepository implements RepositoryLog {

    private static final String LOG_FILE = "./historial_tanque.txt";
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

    @Override
    public List<String> readAllLogs() {
        try {
            return java.nio.file.Files.readAllLines(java.nio.file.Paths.get(LOG_FILE));
        } catch (IOException e) {
            return new java.util.ArrayList<>();
        }
    }
}
