package ru.gur.archbilling.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.gur.archbilling.entity.Account;
import ru.gur.archbilling.persistence.AccountRepository;
import ru.gur.archbilling.service.data.AccountData;
import ru.gur.archbilling.service.immutable.ImmutablePaymentRequest;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@ConditionalOnMissingBean(AccountServiceHW09Impl.class)
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public UUID createAccount() {
        final Account account = new Account();
        account.setBalance(BigDecimal.ZERO);

        return accountRepository.save(account).getId();
    }

    @Override
    public AccountData getAccountInfo(final UUID accountId) {
        Assert.notNull(accountId, "accountId must not be null");

        return accountRepository.findById(accountId)
                .map(a -> AccountData.builder()
                        .id(a.getId())
                        .balance(a.getBalance())
                        .created(a.getCreated())
                        .updated(a.getUpdated())
                        .build())
                .orElseThrow(() -> new RuntimeException("account not found"));
    }

    @Override
    @Transactional
    public UUID makePayment(final ImmutablePaymentRequest paymentRequest) {
        Assert.notNull(paymentRequest, "paymentRequest must not be null");

        final Account account = accountRepository.findByIdLocked(paymentRequest.getId())
                .orElseThrow(() -> new RuntimeException("account not found"));

        if (account.getBalance().compareTo(paymentRequest.getAmount()) < 0) {
            throw new RuntimeException("not enough money on the balance");
        }

        account.setBalance(account.getBalance().subtract(paymentRequest.getAmount()));
        accountRepository.save(account);

        return account.getId();
    }

    @Override
    public UUID makePayment(ImmutablePaymentRequest paymentRequest, UUID orderId) {
        return makePayment(paymentRequest);
    }

    @Override
    @Transactional
    public Double increaseBalance(final UUID accountId, final Double amount) {
        Assert.notNull(accountId, "accountId must not be null");
        Assert.notNull(amount, "amount must not be null");
        Assert.state(amount > 0, "amount must be positive");

        final Account account = accountRepository.findByIdLocked(accountId)
                .orElseThrow(() -> new RuntimeException("account not found"));

        account.setBalance(account.getBalance().add(BigDecimal.valueOf(amount)));
        accountRepository.save(account);

        return account.getBalance().doubleValue();
    }
}
