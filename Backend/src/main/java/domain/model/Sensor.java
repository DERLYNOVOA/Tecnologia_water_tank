package domain.model;

import domain.event.EventHandler;
import domain.event.SensorTypeEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class Sensor {
    private boolean isActive;
    private EventHandler handler;

    public Sensor(EventHandler handler) {
        this.handler = handler;
        this.isActive = false;
    }

    public boolean isActive() {
        return isActive;
    }
    public void setActive(boolean active) {
        this.isActive = active;
    }

    public EventHandler getHandler() {
        return handler;
    }

    public void setHandler(EventHandler handler) {
        this.handler = handler;
    }

    protected abstract SensorTypeEvent getSensorType();
    protected abstract String getSensorDetail();

    public void handleSensor() {
        if (isActive()) {
            Event event = new Event(UUID.randomUUID(),getSensorType(),getSensorDetail(), LocalDateTime.now());
            getHandler().emitEvent(event);
        }
    }

}
