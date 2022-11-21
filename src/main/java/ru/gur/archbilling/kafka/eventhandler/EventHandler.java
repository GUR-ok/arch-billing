package ru.gur.archbilling.kafka.eventhandler;

import ru.gur.archbilling.kafka.event.EventSource;

public interface EventHandler<T extends EventSource> {

    boolean canHandle(EventSource eventSource);

    String handleEvent(T eventSource);
}
