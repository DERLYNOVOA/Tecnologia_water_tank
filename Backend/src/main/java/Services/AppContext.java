package Services;

import Domain.EventHandler;
import Domain.WaterTank;

public class AppContext {
    private WaterTank tank;
    private Authenticator auth;
    private EventHandler events;

    public AppContext(WaterTank tank, Authenticator auth, EventHandler events) {
        this.tank = tank;
        this.auth = auth;
        this.events = events;
    }

    public WaterTank getTank() {
        return tank;
    }

    public Authenticator getAuth() {
        return auth;
    }

    public EventHandler getEvents() {
        return events;
    }
}

