package application.command;

import domain.service.IPump;
import application.service.AppContext;

public class PumpOffCommand implements Command {
    private final IPump pump;

    public PumpOffCommand(IPump pump) {
        this.pump = pump;
    }

    @Override
    public void execute(AppContext context, String arg) throws Exception {
        if (!pump.getStatus()) {
            System.out.println("La bomba ya estaba apagada.");
            return;
        }
        pump.turnOff();
        String user = context.getAuth().getCurrentUser().getUserName();
        context.getLogger().saveLog("[ACTION] " + user + " apagó la bomba manualmente.");
    }
}