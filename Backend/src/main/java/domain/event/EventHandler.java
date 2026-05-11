package domain.event;

import domain.service.EventListener;
import domain.model.Event;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class  EventHandler {

    private List<EventListener> listeners = new CopyOnWriteArrayList<>();

    public void subscribe(EventListener listener) {
        listeners.add(listener);
    }

    public void unsubscribe(EventListener listener) {
        listeners.remove(listener);
    }
    public void emitEvent(Event event) {
        for (EventListener listener : listeners) {
            listener.onEvent(event);
        }
    }
}

