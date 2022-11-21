package ru.gur.archbilling.kafka.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class OrderCreatedEventData implements KafkaEvent {

    UUID orderId;

    UUID accountId;

    Double price;

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Prevents duplication when serializing to JSON (subtype discriminator property)
    public Event getEvent() {
        return Event.ORDER_CREATED;
    }
}
