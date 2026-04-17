package LibraryManager;

public class PumpOnCommand implements Command {
    @Override
    public void execute(AppContext context, String arg) throws Exception {
        // Crear una bomba e inicializarla
        Pump pump = new PumpImpl();
        pump.turnOn();
        System.out.println("Bomba encendida. Estado: " + pump.getStatus());
    }
}

