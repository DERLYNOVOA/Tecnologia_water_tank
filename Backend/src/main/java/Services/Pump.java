package Services;

public class Pump implements IPump {
    private boolean isActive;

    @Override
    public void turnOn() {
        isActive = true;
    }

    @Override
    public void turnOff() {
        isActive = false;
    }

    @Override
    public boolean getStatus() {
        return isActive;
    }
}

