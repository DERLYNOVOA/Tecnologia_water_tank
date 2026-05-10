package domain.service;

public interface IWaterSystemStatus {
    float getCurrentDistance();
    float getCurrentPercentage();
    boolean isPumpActive();
    boolean hasData();
}