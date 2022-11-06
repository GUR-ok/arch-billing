package ru.gur.archbilling.service;

import ru.gur.archbilling.service.data.AccountData;
import ru.gur.archbilling.service.immutable.ImmutablePaymentRequest;

import java.util.UUID;

public interface AccountService {

    UUID createAccount();

    AccountData getAccountInfo(UUID accountId);

    UUID makePayment(ImmutablePaymentRequest paymentRequest);

    Double increaseBalance(UUID accountId, Double amount);
}
