package domain.model;

public class WaterTank {
    private float totalHeight;

    public WaterTank(float totalHeight) {
        this.totalHeight = totalHeight;
    }

    public float calculatePercentage(float currentDistance) {
        if (currentDistance >= totalHeight) return 0.0f;
        if (currentDistance <= 0.0f) return 100.0f;
        return ((totalHeight - currentDistance) / totalHeight) * 100.0f;
    }

}


