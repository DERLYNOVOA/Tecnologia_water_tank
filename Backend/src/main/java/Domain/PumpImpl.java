package Domain;

public class PumpImpl implements Pump {
    private boolean isActive;

    public PumpImpl() {
        this.isActive = false;
    }

    @Override
    public void turnOn() {
        isActive = true;
    }

    @Override
    public void turnOff() {
        isActive = false;
    }

    @Override
    public boolean getStatus() {
        return isActive;
    }
}

