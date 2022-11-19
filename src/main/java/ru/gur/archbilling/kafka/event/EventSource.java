package ru.gur.archbilling.kafka.event;

public interface EventSource {

    Event getEvent();
}
