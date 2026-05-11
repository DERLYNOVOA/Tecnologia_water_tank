package domain.model;

import domain.event.EventHandler;
import domain.event.SensorTypeEvent;


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

    public boolean hasData() {
        return waterLevel > 0;
    }

    @Override
    protected SensorTypeEvent getSensorType() {
        return SensorTypeEvent.WaterLevelEvent;
    }

    @Override
    protected String getSensorDetail() {
        return "Water Level: " + waterLevel;
    }

}

