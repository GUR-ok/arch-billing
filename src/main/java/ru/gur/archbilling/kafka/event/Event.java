package ru.gur.archbilling.kafka.event;

public enum Event {

    ORDER_CREATED,
    PAYMENT_FAIL,
    ORDER_PAID,
    DEPOSIT_ACCEPTED,
    DEPOSIT_REQUEST;
}
