package ru.gur.archbilling.kafka.eventhandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import ru.gur.archbilling.kafka.Producer;
import ru.gur.archbilling.kafka.event.DepositAcceptedEventData;
import ru.gur.archbilling.kafka.event.DepositRequestEventData;
import ru.gur.archbilling.kafka.event.Event;
import ru.gur.archbilling.kafka.event.EventSource;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile({"hw09","local"})
public class DepositRequestHandler implements EventHandler<DepositRequestEventData> {

    private final Producer producer;

    @Override
    public boolean canHandle(final EventSource eventSource) {
        Assert.notNull(eventSource, "EventSource must not be null");

        return Event.DEPOSIT_REQUEST.equals(eventSource.getEvent());
    }

    @Override
    public String handleEvent(final DepositRequestEventData eventSource) {
        Assert.notNull(eventSource, "EventSource must not be null");

        producer.sendString("KeyValueTopic", eventSource.getAccountId().toString(), eventSource.getValue());

        log.info("Event handled: {}", eventSource);

        return eventSource.getEvent().name();
    }
}
