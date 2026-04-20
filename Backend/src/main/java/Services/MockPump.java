package Services;

import Domain.IPump;

public class MockPump implements IPump {

    private boolean isActive;

    public MockPump() {
        this.isActive = false; // La bomba siempre arranca apagada
    }

    @Override
    public void turnOn() {
        if (!isActive) {
            isActive = true;
            System.out.println("==================================================");
            System.out.println(" 🔧 ACCIÓN FÍSICA: ¡BOMBA ENCENDIDA (Simulación)! ");
            System.out.println("==================================================");
        }
    }

    @Override
    public void turnOff() {
        if (isActive) {
            isActive = false;
            System.out.println("==================================================");
            System.out.println(" 🛑 ACCIÓN FÍSICA: ¡BOMBA APAGADA (Simulación)!   ");
            System.out.println("==================================================");
        }
    }

    @Override
    public boolean getStatus() {
        return isActive;
    }
}
