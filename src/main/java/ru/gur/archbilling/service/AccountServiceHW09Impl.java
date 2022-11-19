package ru.gur.archbilling.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.gur.archbilling.entity.Account;
import ru.gur.archbilling.kafka.Producer;
import ru.gur.archbilling.kafka.event.OrderPaidEventData;
import ru.gur.archbilling.kafka.event.PaymentFailEventData;
import ru.gur.archbilling.persistence.AccountRepository;
import ru.gur.archbilling.service.data.AccountData;
import ru.gur.archbilling.service.immutable.ImmutablePaymentRequest;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Primary
@Service
@RequiredArgsConstructor
@Profile("hw09")
public class AccountServiceHW09Impl implements AccountService {

    private static final String TOPIC = "payment";

    private final AccountRepository accountRepository;
    private final Producer producer;
    private final StreamsBuilderFactoryBean factoryBean;

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

        KafkaStreams kafkaStreams = factoryBean.getKafkaStreams();

        final ReadOnlyKeyValueStore<String, Double> readOnlyKeyValueStore =
                kafkaStreams.store(StoreQueryParameters.fromNameAndType("counts", QueryableStoreTypes.keyValueStore()));
        readOnlyKeyValueStore.all()
                .forEachRemaining(x -> System.out.println(x.key + "->" + x.value));

        return Optional.ofNullable(readOnlyKeyValueStore.get(accountId.toString()))
                .map(a -> AccountData.builder()
                        .id(accountId)
                        .balance(BigDecimal.valueOf(a))
                        .build())
                .orElseThrow(() -> new RuntimeException("account balance not found"));
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
        Assert.notNull(paymentRequest, "paymentRequest must not be null");

        final Account account = accountRepository.findByIdLocked(paymentRequest.getId())
                .orElseThrow(() -> new RuntimeException("account not found"));

        if (account.getBalance().compareTo(paymentRequest.getAmount()) < 0) {
            producer.sendEvent(TOPIC, paymentRequest.getId().toString(), PaymentFailEventData.builder()
                    .accountId(paymentRequest.getId())
                    .orderId(orderId)
                    .build());

            throw new RuntimeException("not enough money on the balance");
        }

        producer.sendDouble("KeyValueTopic", paymentRequest.getId().toString(),
                paymentRequest.getAmount().negate().doubleValue());
        producer.sendEvent(TOPIC, paymentRequest.getId().toString(), OrderPaidEventData.builder()
                .orderId(orderId)
                .build());

        return account.getId();
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
