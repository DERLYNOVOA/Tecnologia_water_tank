package Services;

import Domain.IPump;
import Domain.WaterLevelSensor;
import Domain.WaterTank;
import Domain.EventHandler; // <--- Asegúrate de importar esto
import Domain.EventListener; // <--- Y esto

// Agregamos "implements EventListener" para que pueda escuchar
public class WaterLevelManager extends SensorLevelManager implements Domain.EventListener {

    private IPump pump;
    private WaterLevelSensor waterSensor;
    private WaterTank tank;

    //Ahora recibe 4 parámetros (incluye el EventHandler)
    public WaterLevelManager(IPump pump, WaterLevelSensor sensor, WaterTank tank, EventHandler handler) {
        super(20.0f, 80.0f);
        this.pump = pump;
        this.waterSensor = sensor;
        this.tank = tank;

        //Suscribirse al manejador de eventos
        handler.subscribe(this);
    }

    //se ejecuta cuando el sensor grita hay nuevo dato!
    @Override
    public void onEvent(Domain.Event event) {
        run(); // Ejecuta la lógica cada vez que llega un evento
    }

    @Override
    public void run() {
        float distanciaActual = waterSensor.getWaterLevel();
        float porcentaje = tank.calculatePercentage(distanciaActual);

        System.out.println("Nivel: " + porcentaje + "%");

        if (porcentaje <= getMinLevel() && !pump.getStatus()) {
            pump.turnOn();
        } else if (porcentaje >= getMaxLevel() && pump.getStatus()) {
            pump.turnOff();
        }
    }
}

