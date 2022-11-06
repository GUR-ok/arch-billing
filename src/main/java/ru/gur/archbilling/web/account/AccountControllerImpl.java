package ru.gur.archbilling.web.account;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.*;
import ru.gur.archbilling.service.AccountService;
import ru.gur.archbilling.service.immutable.ImmutablePaymentRequest;
import ru.gur.archbilling.web.account.request.PaymentRequest;
import ru.gur.archbilling.web.account.response.GetAccountResponse;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountControllerImpl implements AccountController {

    private final AccountService accountService;
    private final ConversionService conversionService;

    @Override
    @PostMapping("/create")
    @Operation(summary = "Создать новый счет")
    public UUID createAccount() {
        return accountService.createAccount();
    }

    @Override
    @GetMapping("/{id}")
    @Operation(summary = "Получить информацию о счете")
    public GetAccountResponse getAccountInfo(@PathVariable("id") final UUID accountId) {
        return conversionService.convert(accountService.getAccountInfo(accountId), GetAccountResponse.class);
    }

    @Override
    @PostMapping
    @Operation(summary = "Произвести оплату")
    public UUID makePayment(@Valid @NotNull @RequestBody final PaymentRequest paymentRequest) {
        return accountService.makePayment(conversionService.convert(paymentRequest, ImmutablePaymentRequest.class));
    }

    @Override
    @PostMapping("/{id}")
    @Operation(summary = "Зачислить средства")
    public Double increaseBalance(@PathVariable("id") final UUID accountId,
                                  @RequestParam(name = "amount") final Double amount) {
        return accountService.increaseBalance(accountId, amount);
    }
}
