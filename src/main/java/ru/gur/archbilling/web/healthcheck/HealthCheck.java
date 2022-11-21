package ru.gur.archbilling.web.healthcheck;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gur.archbilling.web.healthcheck.response.Health;

@RestController
@RequiredArgsConstructor
public class HealthCheck {

    private final StreamsBuilderFactoryBean factoryBean;

    @GetMapping("/health")
    public Health health() {
        KafkaStreams kafkaStreams = factoryBean.getKafkaStreams();

        kafkaStreams.store(StoreQueryParameters.fromNameAndType("counts", QueryableStoreTypes.keyValueStore()))
                .all()
                .forEachRemaining(x -> System.out.println(x.key + "->" + x.value + "\n"));

        return Health.builder()
                .status(Health.HealthStatus.OK)
                .build();
    }
}
