package LibraryManager;

public interface Command {
    void execute(AppContext context, String arg) throws Exception;
}

