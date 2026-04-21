package Commands;

import Services.AppContext;

public interface Command {
    void execute(AppContext context, String arg) throws Exception;
}

