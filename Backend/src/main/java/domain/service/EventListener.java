package domain.service;

import domain.model.Event;

public interface EventListener {
    void onEvent(Event event);
}

