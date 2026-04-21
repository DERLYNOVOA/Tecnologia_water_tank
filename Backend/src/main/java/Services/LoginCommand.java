package Services;

import java.util.Scanner;

public class LoginCommand implements Command {
    @Override
    public void execute(AppContext context, String arg) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Usuario: ");
        String userName = scanner.nextLine();

        System.out.print("Contraseña: ");
        String password = scanner.nextLine();

        if (context.getAuth().login(userName, password)) {
            System.out.println("✅ Login exitoso.");
            // PERSISTENCIA DETALLADA
            context.getLogger().saveLog("[AUTH] Usuario " + userName + " inició sesión.");
        } else {
            System.out.println("Login fallido. Usuario o contraseña incorrectos");
        }
    }
}


