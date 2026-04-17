package Services;

import java.util.HashMap;

public class CommandHandler {
    private HashMap<String, Command> commands = new HashMap<>();
    private AppContext context;

    public CommandHandler(AppContext context) {
        this.context = context;
    }

    public void registerCommand(String name, Command cmd) {
        commands.put(name, cmd);
    }

    public void executeCommand(String name) throws Exception {
        Command cmd = commands.get(name);
        if (cmd != null) {
            cmd.execute(context);
        } else {
            throw new Exception("Command not found");
        }
    }
}

