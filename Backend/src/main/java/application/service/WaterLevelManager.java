package application.service;

import domain.model.Event;
import domain.event.EventHandler;
import domain.service.EventListener;
import domain.service.IPump;
import domain.service.IWaterSystemStatus;
import domain.model.WaterLevelSensor;
import domain.model.WaterTank;
import domain.service.RepositoryLog;

public class WaterLevelManager extends SensorLevelManager implements EventListener, IWaterSystemStatus {

    private final IPump pump;
    private final WaterLevelSensor waterSensor;
    private final WaterTank tank;
    private final RepositoryLog logger;

    public WaterLevelManager(IPump pump, WaterLevelSensor sensor, WaterTank tank, EventHandler handler, RepositoryLog logger) {
        super(20.0f, 80.0f);
        this.pump = pump;
        this.waterSensor = sensor;
        this.tank = tank;
        this.logger = logger;
        handler.subscribe(this);
    }

    @Override
    public void onEvent(Event event) {
        run();
    }

    @Override
    public void run() {
        if (!waterSensor.hasData()) return;
        float distanciaActual = waterSensor.getWaterLevel();
        float porcentaje      = tank.calculatePercentage(distanciaActual);

        if (porcentaje >= 80.0f && pump.getStatus()) {
            pump.turnOff();
            logger.saveLog("[SEGURIDAD] Apagado automático. Nivel: " + porcentaje + "%. Distancia: " + distanciaActual + "cm.");


        } else if (porcentaje <= 10.0f && !pump.getStatus()) {
            pump.turnOn();
            logger.saveLog("[AUTO] Encendido por nivel bajo. Nivel: " + porcentaje + "%.");
        }
    }

    @Override
    public float getCurrentDistance() {
        return waterSensor.getWaterLevel();
    }

    @Override
    public float getCurrentPercentage() {
        return tank.calculatePercentage(waterSensor.getWaterLevel());
    }

    @Override
    public boolean isPumpActive() {
        return pump.getStatus();
    }

    @Override
    public boolean hasData() {
        return waterSensor.hasData();
    }

}

