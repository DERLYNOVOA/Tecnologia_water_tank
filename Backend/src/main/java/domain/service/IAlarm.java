package domain.service;

public interface IAlarm {
        void turnOn();
        void turnOff();
        boolean isActive();
        void setVolume(int volume);
}
