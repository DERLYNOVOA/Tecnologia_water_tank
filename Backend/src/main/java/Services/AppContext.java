package Services;

import Domain.EventHandler;
import Domain.WaterTank;
import Repository.IRepositoryLog;

public class AppContext {
    private WaterTank tank;
    private Authenticator auth;
    private EventHandler events;
    private IRepositoryLog logger;

    public AppContext(WaterTank tank, Authenticator auth, EventHandler events, IRepositoryLog logger) {
        this.tank = tank;
        this.auth = auth;
        this.events = events;
        this.logger = logger;
    }

    public WaterTank getTank() {return tank;}
    public Authenticator getAuth() {return auth;}
    public EventHandler getEvents() {return events;}
    public IRepositoryLog getLogger() { return logger; }
}

