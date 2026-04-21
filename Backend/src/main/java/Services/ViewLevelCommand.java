package Services;

import Domain.IPump;
import Domain.WaterLevelSensor;
import Domain.WaterTank;

public class ViewLevelCommand implements Command {

    private WaterLevelSensor sensor;
    private IPump pump;

    // Inyectamos el sensor y la bomba desde afuera
    public ViewLevelCommand(WaterLevelSensor sensor, IPump pump) {
        this.sensor = sensor;
        this.pump = pump;
    }

    @Override
    public void execute(AppContext context, String arg) throws Exception {
        // 1. Sacamos el tanque del contexto global
        WaterTank tank = context.getTank();

        try {
            // 2. Leemos los datos reales usando los métodos correctos de tu UML
            float level = sensor.getWaterLevel();
            float percent = tank.calculatePercentage(level);
            boolean pumpOn = pump.getStatus();

            // 3. Imprimimos tu interfaz
            System.out.println("================================");
            System.out.println("        ESTADO DEL TANQUE");
            System.out.println("================================");
            System.out.printf("  Nivel:    %.2f cm (%.1f%%)%n", level, percent);
            System.out.println("  Bomba:    " + (pumpOn ? "ENCENDIDA ✅" : "APAGADA ❌"));
            System.out.println("================================");

        } catch (Exception e) {
            System.out.println("⚠️ Error al leer los datos del sensor. Intente nuevamente.");
        }
    }
}