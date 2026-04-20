package Services;

import Domain.IPump;
import Domain.WaterLevelSensor;
import Domain.WaterTank;
import Domain.EventHandler; // <--- Asegúrate de importar esto
import Domain.EventListener; // <--- Y esto

// 1. IMPORTANTE: Agregamos "implements EventListener" para que pueda escuchar
public class WaterLevelManager extends SensorLevelManager implements Domain.EventListener {

    private IPump pump;
    private WaterLevelSensor waterSensor;
    private WaterTank tank;

    // 2. ACTUALIZADO: Ahora recibe 4 parámetros (incluye el EventHandler)
    public WaterLevelManager(IPump pump, WaterLevelSensor sensor, WaterTank tank, EventHandler handler) {
        super(20.0f, 80.0f);
        this.pump = pump;
        this.waterSensor = sensor;
        this.tank = tank;

        // 3. LA CLAVE: Suscribirse al manejador de eventos
        handler.subscribe(this);
    }

    // 4. NUEVO: El método que se ejecuta cuando el sensor grita "¡Hay nuevo dato!"
    @Override
    public void onEvent(Domain.Event event) {
        run(); // Ejecuta la lógica cada vez que llega un evento
    }

    @Override
    public void run() {
        float distanciaActual = waterSensor.getWaterLevel();
        float porcentaje = tank.calculateWaterVolumePercentage(distanciaActual);

        System.out.println("Nivel: " + porcentaje + "%");

        if (porcentaje <= getMinLevel() && !pump.getStatus()) {
            pump.turnOn();
        } else if (porcentaje >= getMaxLevel() && pump.getStatus()) {
            pump.turnOff();
        }
    }
}

