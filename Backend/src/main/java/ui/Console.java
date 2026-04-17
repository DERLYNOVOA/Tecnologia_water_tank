package LibraryManager;

import Commands.CommandHandler;

import java.util.Scanner;

public class Console {
    private CommandHandler commandHandler;
    private Scanner scanner;

    public Console(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Sistema de Gestión de Tanque de Agua");
        System.out.println("Escriba 'help' para ver los comandos disponibles");
        
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            
            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Saliendo del sistema...");
                break;
            }
            
            String[] parts = input.split(" ", 2);
            String command = parts[0];
            String arg = parts.length > 1 ? parts[1] : "";
            
            try {
                commandHandler.execute(command, arg);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        
        scanner.close();
    }
}

