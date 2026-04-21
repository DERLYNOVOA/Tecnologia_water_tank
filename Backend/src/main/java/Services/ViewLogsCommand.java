package Services;

import java.nio.file.*;
import java.util.List;

public class ViewLogsCommand implements Command {
    @Override
    public void execute(AppContext context, String arg) throws Exception {
        System.out.println("\n📜 --- HISTORIAL DE PERSISTENCIA ---");
        try {
            List<String> lineas = Files.readAllLines(Paths.get("./historial_tanque.txt"));
            for (String linea : lineas) {
                System.out.println(linea);
            }
        } catch (Exception e) {
            System.out.println("⚠️ No se encontró el archivo de logs todavía.");
        }
        System.out.println("------------------------------------");
    }
}