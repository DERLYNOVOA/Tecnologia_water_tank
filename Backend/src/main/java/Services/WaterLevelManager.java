package Services;

import Domain.Pump;
import Domain.WaterLevelSensor;

public class WaterLevelManager {
    private IPump IPump;
    private WaterLevelSensor waterSensor;
    private boolean isActive;

    public WaterLevelManager(IPump pump, WaterLevelSensor waterSensor) {
        this.IPump = pump;
        this.waterSensor = waterSensor;
        this.isActive = false;
    }

    public void run() {
        // Lógica para gestionar el nivel de agua
    }

    public IPump getPump() {
        return IPump;
    }

    public WaterLevelSensor getWaterSensor() {
        return waterSensor;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}

