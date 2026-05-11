package application.service;

public abstract class SensorLevelManager {
    //private float level;
    private float minLevel;
    private float maxLevel;
    private boolean isActive;

    public SensorLevelManager(float minLevel, float maxLevel) {
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.isActive = false;
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

    public abstract void run();
}

