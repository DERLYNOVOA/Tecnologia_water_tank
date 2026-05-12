package domain.service;

public interface IPumpStrategy {
    boolean shouldTurnOn(float percentage);
    boolean shouldTurnOff(float percentage);
}
