package ru.gur.archbilling.kafka.eventhandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import ru.gur.archbilling.kafka.Producer;
import ru.gur.archbilling.kafka.event.Event;
import ru.gur.archbilling.kafka.event.EventSource;
import ru.gur.archbilling.kafka.event.PaymentFailEventData;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile({"hw09","local"})
public class PaymentFailHandler implements EventHandler<PaymentFailEventData> {

    private final Producer producer;

    @Override
    public boolean canHandle(final EventSource eventSource) {
        Assert.notNull(eventSource, "EventSource must not be null");

        return Event.PAYMENT_FAIL.equals(eventSource.getEvent());
    }

    @Override
    public String handleEvent(final PaymentFailEventData eventSource) {
        Assert.notNull(eventSource, "EventSource must not be null");

//        service.call

        log.info("Event handled: {}", eventSource);

        return eventSource.getEvent().name();
    }
}
