package Services;

import Domain.EventListener;
import Domain.Event;
import Domain.IPump;
import Domain.IWaterSystemStatus;
import Domain.WaterLevelSensor;
import Domain.WaterTank;
import Domain.EventHandler;
import Repository.IRepositoryLog;

public class WaterLevelManager extends SensorLevelManager implements EventListener, IWaterSystemStatus {

    private final IPump pump;
    private final WaterLevelSensor waterSensor;
    private final WaterTank tank;
    private final IRepositoryLog logger;

    public WaterLevelManager(IPump pump, WaterLevelSensor sensor, WaterTank tank, EventHandler handler, IRepositoryLog logger) {
        super(20.0f, 80.0f);
        this.pump = pump;
        this.waterSensor = sensor;
        this.tank = tank;
        this.logger = logger;
        handler.subscribe(this);
    }

    // EventListener
    @Override
    public void onEvent(Event event) {
        run();
    }

    // Lógica de automatización
    @Override
    public void run() {
        if (!waterSensor.hasData()) return;
        float distanciaActual = waterSensor.getWaterLevel();
        float porcentaje      = tank.calculatePercentage(distanciaActual);

        if (porcentaje >= 90.0f && pump.getStatus()) {
            pump.turnOff();
            logger.saveLog("[SEGURIDAD] Apagado automático. Nivel: " + porcentaje + "%. Distancia: " + distanciaActual + "cm.");


        } else if (porcentaje <= 10.0f && !pump.getStatus()) {
            pump.turnOn();
            logger.saveLog("[AUTO] Encendido por nivel bajo. Nivel: " + porcentaje + "%.");
        }
    }

    //IWaterSystemStatus para lectura para la UI
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

