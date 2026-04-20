package Services;

import Domain.TemperatureSensor;

public class TemperatureManager {
    private TemperatureSensor temperatureSensor;
    private float minLevel;
    private float maxLevel;
    private boolean isActive;

    public TemperatureManager(TemperatureSensor temperatureSensor, float minLevel, float maxLevel) {
        this.temperatureSensor = temperatureSensor;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.isActive = false;
    }

    public void run() {
        // Lógica para gestionar la temperatura
    }

    public void setTemperature(float temperature) {
        temperatureSensor.setTemperature(temperature);
    }

    public TemperatureSensor getTemperatureSensor() {
        return temperatureSensor;
    }

    public float getMinLevel() {
        return minLevel;
    }

    public float getMaxLevel() {
        return maxLevel;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}


