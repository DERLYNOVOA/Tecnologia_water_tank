package Services;

public class WaterLevelSensor extends Sensor {
    public WaterLevelSensor(EventHandler handler, boolean isActive) {
        this.handler = handler;
        this.isActive = isActive;
    }

    public float getWaterLevel() {
        // Implementación específica
        return 0.0f;
    }

    @Override
    public void handleSensor() {
        // Implementación específica
    }
}

