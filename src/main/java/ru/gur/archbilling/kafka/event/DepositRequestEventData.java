package ru.gur.archbilling.kafka.event;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class DepositRequestEventData implements KafkaEvent {

    UUID accountId;

    Double value;

    @Override
    public Event getEvent() {
        return Event.DEPOSIT_REQUEST;
    }
}
