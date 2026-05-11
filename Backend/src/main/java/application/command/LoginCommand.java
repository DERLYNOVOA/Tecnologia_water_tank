package application.command;

import application.service.AppContext;

import java.util.Scanner;

public class LoginCommand implements Command {
    @Override
    public void execute(AppContext context, String arg) {
        String[] parts = arg.split(" ", 2);
        if (parts.length < 2) {
            System.out.println("  Formato inválido.");
            return;
        }
        String userName = parts[0];
        String password = parts[1];

        if (context.getAuth().login(userName, password)) {
            System.out.println("  Login exitoso.");
            context.getLogger().saveLog("[AUTH] Usuario " + userName + " inició sesión.");
        } else {
            System.out.println("  Login fallido. Usuario o contraseña incorrectos.");
        }
    }
}


