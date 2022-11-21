package ru.gur.archbilling.service.immutable;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Value
@Builder
public class ImmutablePaymentRequest {

    /**
     * accountId
     */
    UUID id;

    BigDecimal amount;
}
