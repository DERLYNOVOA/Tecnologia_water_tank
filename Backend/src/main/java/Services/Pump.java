package Services;

import Domain.IPump;

public class Pump implements IPump {
    private boolean isActive;

    public Pump() {
        this.isActive = false;
    }

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

