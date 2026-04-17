package Services;

public class WaterTank {
    private float capacity;

    public WaterTank(float capacity) {
        this.capacity = capacity;
    }

    public float calculateWaterVolumePercentage(float currentLevel) {
        return (currentLevel / capacity) * 100;
    }
}

