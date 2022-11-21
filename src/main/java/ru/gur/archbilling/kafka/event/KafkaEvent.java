package ru.gur.archbilling.kafka.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "event"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = OrderCreatedEventData.class, name = "ORDER_CREATED"),
        @JsonSubTypes.Type(value = PaymentFailEventData.class, name = "PAYMENT_FAIL"),
        @JsonSubTypes.Type(value = OrderPaidEventData.class, name = "ORDER_PAID"),
        @JsonSubTypes.Type(value = DepositAcceptedEventData.class, name = "DEPOSIT_ACCEPTED"),
        @JsonSubTypes.Type(value = DepositRequestEventData.class, name = "DEPOSIT_REQUEST")
})
public interface KafkaEvent extends EventSource{
}