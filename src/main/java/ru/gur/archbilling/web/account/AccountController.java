package ru.gur.archbilling.web.account;

import ru.gur.archbilling.web.account.request.PaymentRequest;
import ru.gur.archbilling.web.account.response.GetAccountResponse;

import java.util.UUID;

public interface AccountController {

    UUID createAccount();

    GetAccountResponse getAccountInfo(UUID accountId);

    UUID makePayment(PaymentRequest paymentRequest);

    Double increaseBalance(UUID accountId, Double amount);
}
