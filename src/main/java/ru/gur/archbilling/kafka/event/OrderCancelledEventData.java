package ru.gur.archbilling.kafka.event;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class OrderCancelledEventData implements KafkaEvent {

    UUID orderId;

    @Override
    public Event getEvent() {
        return Event.ORDER_CANCEL;
    }
}

