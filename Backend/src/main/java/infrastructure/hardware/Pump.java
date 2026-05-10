package infrastructure.hardware;

import domain.service.IPump;

public class Pump implements IPump {
    private boolean isActive;
    private final ArduinoSerial conexion;

    public Pump(ArduinoSerial conexion) {
        this.isActive = false;
        this.conexion = conexion;
    }

    @Override
    public void turnOn() {
        isActive = true;
        if (conexion != null) conexion.enviarComando("1");
    }

    @Override
    public void turnOff() {
        isActive = false;
        if (conexion != null) conexion.enviarComando("0");
    }

    @Override
    public boolean getStatus() {
        return isActive;
    }
}

