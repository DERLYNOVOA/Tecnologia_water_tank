package application.command;

import domain.service.IWaterSystemStatus;
import application.service.AppContext;

public class ViewLevelCommand implements Command {

    private final IWaterSystemStatus status;

    public ViewLevelCommand(IWaterSystemStatus status) {
        this.status = status;
    }

    @Override
    public void execute(AppContext context, String arg) throws Exception {
        float level   = status.getCurrentDistance();
        float percent = status.getCurrentPercentage();
        boolean pumpOn = status.isPumpActive();

        System.out.println("\n╔══════════════════════════════════════════════╗");
        System.out.println("║               ESTADO DEL TANQUE                ║");
        System.out.println("╠════════════════════════════════════════════════╣");
        System.out.printf( "║  Distancia : %6.2f cm                          ║%n", level);
        System.out.printf( "║  Nivel     : %5.1f %%                          ║%n", percent);
        System.out.println("║  Bomba: " + (pumpOn ? "ENCENDIDA ║" : "APAGADA ║"));
        System.out.println("╚════════════════════════════════════════════════╝\n");
    }
}