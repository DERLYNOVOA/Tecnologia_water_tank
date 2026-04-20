package Services;

public class SensorLevelManager {
    private float level;
    private float minLevel;
    private float maxLevel;
    private boolean isActive;

    public SensorLevelManager(float minLevel, float maxLevel) {
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.level = 0.0f;
        this.isActive = false;
    }

    public float getLevel() {
        return level;
    }

    public void setLevel(float level) {
        this.level = level;
    }

    public float getMinLevel() {
        return minLevel;
    }

    public float getMaxLevel() {
        return maxLevel;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void run() {
        // Lógica para gestionar el nivel del sensor
    }
}

