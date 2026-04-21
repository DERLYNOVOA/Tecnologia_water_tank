package Services;

import Domain.IPump;
import ArduinoComm.ArduinoSerial;

public class Pump implements IPump {
    private boolean isActive;
    private ArduinoSerial conexion; // Usaremos la conexión

    // Ahora recibe la conexión en el constructor
    public Pump(ArduinoSerial conexion) {
        this.isActive = false;
        this.conexion = conexion;
    }

    @Override
    public void turnOn() {
        isActive = true;
        if (conexion != null) conexion.enviarComando("1"); // Le grita al Arduino: ¡Prende!
    }

    @Override
    public void turnOff() {
        isActive = false;
        if (conexion != null) conexion.enviarComando("0"); // Le grita al Arduino: ¡Apaga!
    }

    @Override
    public boolean getStatus() {
        return isActive;
    }
}

