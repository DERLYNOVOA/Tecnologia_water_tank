package application.command;

import application.service.AppContext;

public interface Command {
    void execute(AppContext context, String arg) throws Exception;
}

