package Domain;

import Domain.SensorTypeEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public class Event {
    private UUID id;
    private SensorTypeEvent type;
    private String detail;
    private LocalDateTime timestamp;

    public Event(UUID id, SensorTypeEvent type, String detail, LocalDateTime timestamp) {
        this.id = id;
        this.type = type;
        this.detail = detail;
        this.timestamp = timestamp;
    }

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    public SensorTypeEvent getType() {
        return type;
    }
    public void setType(SensorTypeEvent type) {
        this.type = type;
    }

    public String getDetail() {
        return detail;
    }
    public void setDetail(String detail) {
        this.detail = detail;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

