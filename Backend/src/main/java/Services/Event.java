package Services;

import java.util.UUID;
import java.time.LocalDateTime;

public class Event {
    private UUID eventId;
    private String userName;
    private SensorTypeEvent type;
    private String detail;
    private LocalDateTime timestamp;

    public Event(UUID eventId, String userName, SensorTypeEvent type, String detail, LocalDateTime timestamp) {
        this.eventId = eventId;
        this.userName = userName;
        this.type = type;
        this.detail = detail;
        this.timestamp = timestamp;
    }

    // Getters y setters
    public UUID getEventId() { return eventId; }
    public String getUserName() { return userName; }
    public SensorTypeEvent getType() { return type; }
    public String getDetail() { return detail; }
    public LocalDateTime getTimestamp() { return timestamp; }
}

