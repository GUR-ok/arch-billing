package ru.gur.archbilling.service.data;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder
public class AccountData {

    UUID id;

    BigDecimal balance;

    LocalDateTime updated;

    LocalDateTime created;
}
