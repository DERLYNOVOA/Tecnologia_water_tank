package Services;

import Domain.EventHandler;

public class TemperatureSensor extends Sensor {
    private float temperature;

    public TemperatureSensor(EventHandler handler, float temperature, boolean isActive) {
        this.handler = handler;
        this.temperature = temperature;
        this.isActive = isActive;
    }

    public float getTemperature() {
        return temperature;
    }

    @Override
    public void handleSensor() {
        // Implementación específica
    }
}

