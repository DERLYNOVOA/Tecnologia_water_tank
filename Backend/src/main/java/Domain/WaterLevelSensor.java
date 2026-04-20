package Domain;

public class WaterLevelSensor extends Sensor {
    private float waterLevel;

    public WaterLevelSensor(EventHandler handler) {
        super(handler);
        this.waterLevel = 0.0f;
    }

    public float getWaterLevel() {
        return waterLevel;
    }

    public void setWaterLevel(float waterLevel) {
        this.waterLevel = waterLevel;
        handleSensor();
    }

    @Override
    public void handleSensor() {
        if (isActive) {
            Event event = new Event(java.util.UUID.randomUUID(), SensorTypeEvent.WaterLevelEvent, "WaterLevel: " + waterLevel, java.time.LocalDateTime.now());
            handler.emit(event);
        }
    }
}

