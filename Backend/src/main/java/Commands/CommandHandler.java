package Commands;


import java.util.HashMap;
import java.util.Map;
import Services.AppContext;


public class CommandHandler {
    private Map<String, Command> commands = new HashMap<>();
    private AppContext context;

    public CommandHandler(AppContext context) {
        this.context = context;
    }

    public void registerCommand(String cmd, Command command) {
        commands.put(cmd, command);
    }

    public void execute(String cmd, String arg) throws Exception {
        if (commands.containsKey(cmd)) {
            commands.get(cmd).execute(context, arg);
        } else {
            throw new Exception("Command not found");
        }
    }
}

