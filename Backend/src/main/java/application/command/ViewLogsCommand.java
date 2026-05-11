package application.command;

import application.service.AppContext;

import java.nio.file.*;
import java.util.List;

public class ViewLogsCommand implements Command {

    @Override
    public void execute(AppContext context, String arg) throws Exception {
        System.out.println("\n  --------- HISTORIAL DE PERSISTENCIA ---------");
        List<String> logs = context.getLogger().readAllLogs();
        if (logs.isEmpty()) {
            System.out.println("No se encontraron logs todavía.");
        } else {
            for (String linea : logs) {
                System.out.println(linea);
            }
        }
        System.out.println("------------------------------------------------");
    }
}