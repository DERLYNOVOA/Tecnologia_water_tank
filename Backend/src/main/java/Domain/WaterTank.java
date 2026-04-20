package Domain;

public class WaterTank {
    private WaterLevelManager waterManager;
    private TemperatureManager temperatureManager;
    private float capacity;

    public WaterTank(WaterLevelSensor waterSensor, TemperatureSensor temperatureSensor, float capacity) {
        this.capacity = capacity;
        
        // Crear managers
        Pump pump = new PumpImpl();
        this.waterManager = new WaterLevelManager(pump, waterSensor);
        this.temperatureManager = new TemperatureManager(temperatureSensor, 0, 100);
    }

    public float calculateWaterVolumePercentage(float currentLevel) {
        return (currentLevel / capacity) * 100.0f;
    }

    public WaterLevelManager getWaterManager() {
        return waterManager;
    }

    public TemperatureManager getTemperatureManager() {
        return temperatureManager;
    }

    public float getCapacity() {
        return capacity;
    }
}


