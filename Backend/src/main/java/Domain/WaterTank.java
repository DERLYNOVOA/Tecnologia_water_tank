package Domain;

public class WaterTank {
    private float totalHeight;

    public WaterTank(float totalHeight) {
        this.totalHeight = totalHeight;
    }

    public float calculatePercentage(float currentDistance) {
        // si la distancia es mayor a la altura (ruido del sensor), el tanque está al 0%
        if (currentDistance >= totalHeight) return 0.0f;

        return ((totalHeight - currentDistance) / totalHeight) * 100.0f;
    }
}


