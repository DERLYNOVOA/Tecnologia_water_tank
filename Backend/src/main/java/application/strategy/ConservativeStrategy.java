package application.strategy;

import domain.service.IPumpStrategy;

// Enciende la bomba al 10% y la apaga al 80% — comportamiento original del sistema
public class ConservativeStrategy implements IPumpStrategy {

    @Override
    public boolean shouldTurnOn(float percentage) {
        return percentage <= 10.0f;
    }

    @Override
    public boolean shouldTurnOff(float percentage) {
        return percentage >= 80.0f;
    }
}
