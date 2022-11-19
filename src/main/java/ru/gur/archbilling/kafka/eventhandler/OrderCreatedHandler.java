package ru.gur.archbilling.kafka.eventhandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import ru.gur.archbilling.kafka.event.Event;
import ru.gur.archbilling.kafka.event.EventSource;
import ru.gur.archbilling.kafka.event.OrderCreatedEventData;
import ru.gur.archbilling.service.AccountService;
import ru.gur.archbilling.service.immutable.ImmutablePaymentRequest;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile({"hw09","local"})
public class OrderCreatedHandler implements EventHandler<OrderCreatedEventData> {

    private final AccountService accountService;

    @Override
    public boolean canHandle(final EventSource eventSource) {
        Assert.notNull(eventSource, "EventSource must not be null");

        return Event.ORDER_CREATED.equals(eventSource.getEvent());
    }

    @Override
    public String handleEvent(final OrderCreatedEventData eventSource) {
        Assert.notNull(eventSource, "EventSource must not be null");

        accountService.makePayment(ImmutablePaymentRequest.builder()
                .id(eventSource.getAccountId())
                .amount(BigDecimal.valueOf(eventSource.getPrice()))
            .build());

        log.info("Event handled: {}", eventSource);

        return eventSource.getEvent().name();
    }
}
