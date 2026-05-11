package domain.service;

import java.util.List;

public interface RepositoryLog {
    void saveLog(String message);
    List<String> readAllLogs();
}
