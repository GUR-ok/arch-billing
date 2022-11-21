package ru.gur.archbilling.kafka.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class PaymentFailEventData implements KafkaEvent {

    UUID orderId;

    UUID accountId;

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Prevents duplication when serializing to JSON (subtype discriminator property)
    public Event getEvent() {
        return Event.PAYMENT_FAIL;
    }
}
