package Services;

import Domain.*;
import Repository.IRepositoryLog;

public class AppContext {
    private final Authenticator auth;
    private final EventHandler  handler;
    private final IRepositoryLog logger;

    public AppContext(Authenticator auth, EventHandler handler, IRepositoryLog logger) {
        this.auth    = auth;
        this.handler = handler;
        this.logger  = logger;
    }

    public Authenticator  getAuth()    { return auth; }
    public EventHandler   getHandler() { return handler; }
    public IRepositoryLog getLogger()  { return logger; }
}
