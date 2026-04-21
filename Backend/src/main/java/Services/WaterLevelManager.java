package Services;

import Domain.IPump;
import Domain.WaterLevelSensor;
import Domain.WaterTank;
import Domain.EventHandler;
import Repository.IRepositoryLog; // Importamos la interfaz del logger

public class WaterLevelManager extends SensorLevelManager implements Domain.EventListener {

    private IPump pump;
    private WaterLevelSensor waterSensor;
    private WaterTank tank;
    private IRepositoryLog logger; // Añadimos el logger como atributo

    public WaterLevelManager(IPump pump, WaterLevelSensor sensor, WaterTank tank, EventHandler handler, IRepositoryLog logger) {
        super(20.0f, 80.0f);
        this.pump = pump;
        this.waterSensor = sensor;
        this.tank = tank;
        this.logger = logger; // Lo guardamos

        handler.subscribe(this);
    }

    @Override
    public void onEvent(Domain.Event event) {
        run();
    }

    @Override
    public void run() {
        float distanciaActual = waterSensor.getWaterLevel();
        float porcentaje = tank.calculatePercentage(distanciaActual);

        // SEGURIDAD: Apagado por nivel alto (Mano cerca)
        if (porcentaje >= 90.0f && pump.getStatus()) {
            pump.turnOff();
            // Persistencia detallada: Fecha, Origen, Evento y Valor
            logger.saveLog("[SISTEMA_SEGURIDAD] Apagado automático preventivo. Nivel: " + porcentaje + "%. Distancia: " + distanciaActual + "cm.");
            System.out.println("\n\u001B[31m[ALERTA] Límite alcanzado. Bomba desactivada.\u001B[0m");
            System.out.print("Cyseth> ");
        }

        // AUTOMATISMO: Encendido por nivel bajo (Mano lejos)
        else if (porcentaje <= 10.0f && !pump.getStatus()) {
            pump.turnOn();
            logger.saveLog("[SISTEMA_AUTO] Encendido automático por nivel bajo. Nivel: " + porcentaje + "%.");
            System.out.println("\n\u001B[32m[INFO] Nivel bajo detectado. Bomba activada.\u001B[0m");
            System.out.print("Cyseth> ");
        }
    }
}

