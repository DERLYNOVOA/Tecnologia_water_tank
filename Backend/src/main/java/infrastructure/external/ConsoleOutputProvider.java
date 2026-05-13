package infrastructure.external;

import application.port.outbound.OutputProvider;

public class ConsoleOutputProvider implements OutputProvider {

    @Override
    public void print(String message) {
        System.out.print(message);
    }
    @Override
    public void println(String message) {
        System.out.println(message);
    }
    @Override
    public void printf(String format, Object... args) {
        System.out.printf(format, args);
    }
}