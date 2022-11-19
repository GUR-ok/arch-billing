package ru.gur.archbilling.kafka.event;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class DepositAcceptedEventData implements KafkaEvent {

    UUID accountId;

    @Override
    public Event getEvent() {
        return Event.DEPOSIT_ACCEPTED;
    }
}
