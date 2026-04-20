package Domain;

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
    public void handleSensor() {
        if (isActive) {
            Event event = new Event(java.util.UUID.randomUUID(), SensorTypeEvent.TemperatureEvent, "Temperature: " + temperature, java.time.LocalDateTime.now());
            handler.emitEvent(event);
        }
    }
}

