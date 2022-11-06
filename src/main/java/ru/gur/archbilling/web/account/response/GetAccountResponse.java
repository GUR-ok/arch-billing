package ru.gur.archbilling.web.account.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class GetAccountResponse {

    private UUID id;

    private BigDecimal balance;

    private LocalDateTime updated;

    private LocalDateTime created;
}
