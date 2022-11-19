package ru.gur.archbilling.kafka.eventhandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import ru.gur.archbilling.kafka.event.DepositAcceptedEventData;
import ru.gur.archbilling.kafka.event.Event;
import ru.gur.archbilling.kafka.event.EventSource;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile({"hw09","local"})
public class DepositAcceptedHandler implements EventHandler<DepositAcceptedEventData> {

//    private final service;

    @Override
    public boolean canHandle(final EventSource eventSource) {
        Assert.notNull(eventSource, "EventSource must not be null");

        return Event.DEPOSIT_ACCEPTED.equals(eventSource.getEvent());
    }

    @Override
    public String handleEvent(final DepositAcceptedEventData eventSource) {
        Assert.notNull(eventSource, "EventSource must not be null");

//        service.call

        log.info("Event handled: {}", eventSource);

        return eventSource.getEvent().name();
    }
}
