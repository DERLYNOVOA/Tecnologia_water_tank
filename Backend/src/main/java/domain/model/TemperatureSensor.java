package domain.model;

import domain.event.EventHandler;
import domain.event.SensorTypeEvent;

public class TemperatureSensor extends Sensor {
    private float temperature;

    public TemperatureSensor(EventHandler handler) {
        super(handler);
        this.temperature = 0.0f;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
        handleSensor();
    }

    @Override
    protected SensorTypeEvent getSensorType() {
        return SensorTypeEvent.TemperatureEvent;
    }

    @Override
    protected String getSensorDetail() {
        return "Temperature: " + temperature;
    }
}

