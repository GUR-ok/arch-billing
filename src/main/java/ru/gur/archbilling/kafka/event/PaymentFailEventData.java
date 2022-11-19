package ru.gur.archbilling.kafka.event;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class PaymentFailEventData implements KafkaEvent {

    UUID orderId;

    UUID accountId;

    @Override
    public Event getEvent() {
        return Event.PAYMENT_FAIL;
    }
}
