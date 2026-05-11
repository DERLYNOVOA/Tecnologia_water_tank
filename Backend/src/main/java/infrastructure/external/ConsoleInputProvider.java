package infrastructure.external;

import application.port.outbound.InputProvider;
import java.util.Scanner;

public class ConsoleInputProvider implements InputProvider {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public String readLine() {
        return scanner.nextLine();
    }
}