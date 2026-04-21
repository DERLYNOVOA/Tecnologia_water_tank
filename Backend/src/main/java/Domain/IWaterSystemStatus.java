package Domain;

// Domain/IWaterSystemStatus.java
public interface IWaterSystemStatus {
    float getCurrentDistance();
    float getCurrentPercentage();
    boolean isPumpActive();
    boolean hasData();           // ← nuevo
}