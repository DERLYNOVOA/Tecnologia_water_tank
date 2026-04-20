import Commands.CommandHandler;
import Commands.ViewLevelCommand;
import Commands.PumpOnCommand;
import Commands.LoginCommand;
import Domain.WaterTank;
import Domain.StubWaterLevelSensor;
import Domain.StubTemperatureSensor;
import ui.Console;

/**
 * Punto de entrada de la aplicación.
 * Inyección manual de dependencias:
 * se instancian todos los objetos aquí y se pasan
 * hacia adentro - ninguna clase crea sus propias dependencias.
 */
public class Main {

    public static void main(String[] args) {

        // 1. Crear sensores
        StubWaterLevelSensor waterSensor = new StubWaterLevelSensor();
        StubTemperatureSensor tempSensor = new StubTemperatureSensor();

        // 2. Crear dominio
        WaterTank waterTank = new WaterTank(waterSensor, tempSensor, 100.0f);

        // 3. Registrar comandos en el CommandHandler (Patrón Command)
        CommandHandler handler = new CommandHandler();
        handler.register("nivel", new ViewLevelCommand(waterTank));
        handler.register("bomba", new PumpOnCommand());
        handler.register("login", new LoginCommand());

        // 4. Inyectar handler en la UI (Indirección)
        Console console = new Console(handler);
        console.start();
    }
}
