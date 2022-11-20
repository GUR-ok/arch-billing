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
import ru.gur.archbilling.kafka.event.DepositRequestEventData;
import ru.gur.archbilling.kafka.event.OrderPaidEventData;
import ru.gur.archbilling.kafka.event.PaymentFailEventData;
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

    private final Producer producer;
    private final StreamsBuilderFactoryBean factoryBean;

    @Override
    public UUID createAccount() {
        final UUID uuid = UUID.randomUUID();
        producer.sendDouble("KeyValueTopic", uuid.toString(), 0d);

        return uuid;
    }

    @Override
    public AccountData getAccountInfo(final UUID accountId) {
        Assert.notNull(accountId, "accountId must not be null");

        final KafkaStreams kafkaStreams = factoryBean.getKafkaStreams();
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
        return UUID.randomUUID();
    }

    @Override
    public UUID makePayment(ImmutablePaymentRequest paymentRequest, UUID orderId) {
        Assert.notNull(paymentRequest, "paymentRequest must not be null");

        final BigDecimal balance = BigDecimal.valueOf(getBalance(paymentRequest.getId()));

        if (balance.compareTo(paymentRequest.getAmount()) < 0) {
            System.out.println("!!! not enough money" + paymentRequest);
            producer.sendEvent(TOPIC, paymentRequest.getId().toString(), PaymentFailEventData.builder()
                    .accountId(paymentRequest.getId())
                    .orderId(orderId)
                    .build());

            return paymentRequest.getId();
        }

        producer.sendDouble("KeyValueTopic", paymentRequest.getId().toString(),
                paymentRequest.getAmount().negate().doubleValue());
        producer.sendEvent(TOPIC, paymentRequest.getId().toString(), OrderPaidEventData.builder()
                .orderId(orderId)
                .build());

        return paymentRequest.getId();
    }

    @Override
    @Transactional
    public Double increaseBalance(final UUID accountId, final Double amount) {
        Assert.notNull(accountId, "accountId must not be null");
        Assert.notNull(amount, "amount must not be null");
        Assert.state(amount > 0, "amount must be positive");

        final Double balance = getBalance(accountId);
        producer.sendEvent("billing", accountId.toString(), DepositRequestEventData.builder()
                .accountId(accountId)
                .value(amount)
                .build()
        );

        return Double.sum(balance, amount);
    }

    private Double getBalance(final UUID accountId) {
        final KafkaStreams kafkaStreams = factoryBean.getKafkaStreams();
        final ReadOnlyKeyValueStore<String, Double> readOnlyKeyValueStore =
                kafkaStreams.store(StoreQueryParameters.fromNameAndType("counts", QueryableStoreTypes.keyValueStore()));

        return Optional.ofNullable(readOnlyKeyValueStore.get(accountId.toString()))
                .orElseThrow(() -> new RuntimeException("account not found"));
    }
}
