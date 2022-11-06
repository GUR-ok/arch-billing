package ru.gur.archbilling.web.account.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class PaymentRequest {

    @NotNull
    private UUID accountId;

    @NotNull
    @Positive
    private BigDecimal amount;
}
