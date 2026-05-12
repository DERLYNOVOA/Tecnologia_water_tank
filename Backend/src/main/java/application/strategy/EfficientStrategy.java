package application.strategy;

import domain.service.IPumpStrategy;

// Enciende la bomba al 20% y la apaga al 90% — mantiene el tanque con mayor reserva
public class EfficientStrategy implements IPumpStrategy {

    @Override
    public boolean shouldTurnOn(float percentage) {
        return percentage <= 20.0f;
    }

    @Override
    public boolean shouldTurnOff(float percentage) {
        return percentage >= 90.0f;
    }
}
