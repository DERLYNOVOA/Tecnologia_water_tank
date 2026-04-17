package LibraryManager;

import java.util.ArrayList;
import java.util.List;

public class EventHandler {
    private List<EventListener> listeners = new ArrayList<>();

    public void subscribe(EventListener listener) {
        listeners.add(listener);
    }

    public void unsubscribe(EventListener listener) {
        listeners.remove(listener);
    }

    public void emit(Event event) {
        for (EventListener listener : listeners) {
            listener.onEvent(event);
        }
    }
}

