package ui;

import Services.CommandHandler;
import Services.AppContext; // Asegúrate de importar el contexto
import java.util.Scanner;

public class Console {
    private CommandHandler commandHandler;
    private AppContext context; // Necesitamos el contexto para saber quién está logueado
    private Scanner scanner;

    public Console(CommandHandler commandHandler, AppContext context) {
        this.commandHandler = commandHandler;
        this.context = context;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("\n--- SISTEMA CYSETH OS ACTIVADO ---");

        while (true) {
            printMenu(); // Llamamos al método que imprime las opciones
            System.out.print("> ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            // Lógica de logout simple
            if (input.equalsIgnoreCase("logout")) {
                context.getAuth().logout();
                System.out.println("👋 Sesión cerrada correctamente.");
                continue;
            }

            String[] parts = input.split(" ", 2);
            String command = parts[0];
            String arg = parts.length > 1 ? parts[1] : "";

            try {
                commandHandler.execute(command, arg);
            } catch (Exception e) {
                if(!command.isEmpty()) System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println("\n-------------------------------------------");
        if (context.getAuth().getCurrentUser() == null) {
            System.out.println(" [ ESTADO: VISITANTE ]");
            System.out.println(" > login  - Iniciar sesión");
            System.out.println(" > exit   - Salir del programa");
        } else {
            String nombre = context.getAuth().getCurrentUser().getUserName();
            String rol = context.getAuth().getCurrentUser().getRole().toString();
            System.out.println(" [ USUARIO: " + nombre + " | ROL: " + rol + " ]");
            System.out.println(" > ver_nivel      - Ver estado del tanque");
            System.out.println(" > prender_bomba  - Activar bomba (Manual)");
            System.out.println(" > logs           - Ver historial");
            System.out.println(" > logout         - Cerrar sesión");
            System.out.println(" > exit           - Salir del programa");
        }
        System.out.println("-------------------------------------------");
    }
}

