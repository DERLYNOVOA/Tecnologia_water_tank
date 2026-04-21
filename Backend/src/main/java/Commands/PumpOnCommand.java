package Commands;

import Services.Pump;
import Services.AppContext;

public class PumpOnCommand implements Command {
    @Override
    public void execute(AppContext context, String arg) throws Exception {
        // Crear una bomba e inicializarla
        Pump pump = new Pump();
        pump.turnOn();
        System.out.println("Bomba encendida. Estado: " + pump.getStatus());
    }
}

