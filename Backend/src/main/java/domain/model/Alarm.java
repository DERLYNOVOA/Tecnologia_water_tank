package domain.model;

import domain.service.IAlarm;
import infrastructure.hardware.ArduinoSerial;

public class Alarm implements IAlarm {

    private boolean isActive;
    private int volume;
    private final ArduinoSerial conexion;
    public Alarm( ArduinoSerial conexion) {
        this.isActive = false;
        this.volume = 0;
        this.conexion = conexion;
    }

    public boolean isActive() {
        return isActive;
    }

    @Override
    public void turnOn() {
        isActive = true;

        if (conexion != null) conexion.enviarComando("Y2");
    }
    @Override
    public void turnOff() {
        isActive = false;
        if (conexion != null) conexion.enviarComando("Y0"); // Apagar LED
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
}

