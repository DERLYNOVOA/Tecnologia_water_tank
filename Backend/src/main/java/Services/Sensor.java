package Services;

import Domain.EventHandler;

public abstract class Sensor {
    protected boolean isActive;
    protected EventHandler handler;

    public boolean isActive() {
        return isActive;
    }

    public abstract void handleSensor();
}

