package application.service;

import domain.event.EventHandler;
import domain.service.RepositoryLog;

public class AppContext {
    private final Authenticator auth;
    private final EventHandler  handler;
    private final RepositoryLog logger;

    public AppContext(Authenticator auth, EventHandler handler, RepositoryLog logger) {
        this.auth    = auth;
        this.handler = handler;
        this.logger  = logger;
    }

    public Authenticator  getAuth()    { return auth; }
    public EventHandler   getHandler() { return handler; }
    public RepositoryLog getLogger()  { return logger; }
}
