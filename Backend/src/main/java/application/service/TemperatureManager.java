package application.service;

import domain.model.Event;
import domain.model.TemperatureSensor;
import domain.service.EventListener;
import domain.service.IAlarm;
import domain.service.IWaterSystemStatus;

public class TemperatureManager extends SensorLevelManager implements EventListener {
    private final TemperatureSensor temperatureSensor;
    private final IWaterSystemStatus waterStatus;
    private final IAlarm alarm;

    public TemperatureManager(TemperatureSensor sensor, IWaterSystemStatus waterStatus,
                              IAlarm alarm, float minLevel, float maxLevel) {
        super(minLevel, maxLevel);
        this.temperatureSensor = sensor;
        this.waterStatus = waterStatus;
        this.alarm = alarm;
    }

    @Override
    public void onEvent(Event event) { run(); }

    @Override
    public void run() {
        float waterPercentage = waterStatus.getCurrentPercentage();
        if (waterPercentage == 0) return; // sin agua, no hay temperatura que vigilar

        float temp = temperatureSensor.getTemperature();
        // Con poco agua el umbral baja porque se calienta más rápido
        float effectiveMax = waterPercentage < 30f ? getMaxLevel() - 5f : getMaxLevel();

        if (temp > effectiveMax) alarm.turnOn();
        else alarm.turnOff();
    }
}

