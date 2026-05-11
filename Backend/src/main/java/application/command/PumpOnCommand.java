package application.command;

import domain.service.IPump;
import application.service.AppContext;

public class PumpOnCommand implements Command {
    private IPump pump;

    public PumpOnCommand(IPump pump) {
        this.pump = pump;
    }

    @Override
    public void execute(AppContext context, String arg) throws Exception {
        pump.turnOn();
        String user = context.getAuth().getCurrentUser().getUserName();
        context.getLogger().saveLog("[ACTION] " + user + " encendió la bomba manualmente.");
    }
}

