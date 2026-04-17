package LibraryManager;

public abstract class Sensor {
    protected boolean isActive;
    protected EventHandler handler;

    public Sensor(EventHandler handler) {
        this.handler = handler;
        this.isActive = false;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public abstract void handleSensor();
}

