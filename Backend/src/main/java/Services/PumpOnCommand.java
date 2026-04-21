/*package Services;

public class PumpOnCommand implements Command {
    @Override
    public void execute(AppContext context, String arg) throws Exception {
        // Crear una bomba e inicializarla
        Pump pump = new Pump();
        pump.turnOn();
        System.out.println("Bomba encendida manualmente. Estado: " + pump.getStatus());
    }
}*/

package Services;

import Domain.IPump;

public class PumpOnCommand implements Command {
    private IPump pump;

    // Le pasamos la bomba que ya tiene la conexión
    public PumpOnCommand(IPump pump) {
        this.pump = pump;
    }

    @Override
    public void execute(AppContext context, String arg) throws Exception {
        pump.turnOn();
        String user = context.getAuth().getCurrentUser().getUserName();
        // PERSISTENCIA DETALLADA
        context.getLogger().saveLog("[ACTION] " + user + " encendió la bomba manualmente.");
    }
}