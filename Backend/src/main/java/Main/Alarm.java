package LibraryManager;

public class Alarm {
    private boolean isActive;
    private int volume;

    public Alarm() {
        this.isActive = false;
        this.volume = 0;
    }

    public boolean isActive() {
        return isActive;
    }

    public void turnOn() {
        isActive = true;
    }

    public void turnOff() {
        isActive = false;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
}

