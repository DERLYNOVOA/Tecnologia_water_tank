package application.strategy;

import domain.service.IPumpStrategy;

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
