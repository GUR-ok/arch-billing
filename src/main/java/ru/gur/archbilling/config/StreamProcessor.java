package ru.gur.archbilling.config;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
@Profile({"hw09", "local"})
public class StreamProcessor {

    @Value("${kafka.bootstrapAddress}")
    private String SERVER;

    @Autowired
    void buildPipeline(StreamsBuilder builder) {

        KStream<String, Double> keyValueTopic = builder.stream("KeyValueTopic", Consumed.with(Serdes.String(), Serdes.Double()));

        KTable<String, Double> accountBalance = keyValueTopic
                .groupBy((key, word) -> key)
                .reduce(Double::sum, Materialized.as("counts"));

        accountBalance.toStream().to("AccountsWithBalance", Produced.with(Serdes.String(), Serdes.Double()));

        GlobalKTable<String, Double> textLinesGlobalTable =
                builder.globalTable("AccountsWithBalance", Consumed.with(Serdes.String(), Serdes.Double()),
                        Materialized.as("countsGlobal"));
    }

    private Properties getKafkaStreamsConfig() {
        Properties configurations = new Properties();
        configurations.put(StreamsConfig.APPLICATION_ID_CONFIG, "arch-notification");
        configurations.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, SERVER);
        configurations.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        configurations.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.Double().getClass().getName());
        configurations.put(StreamsConfig.REQUEST_TIMEOUT_MS_CONFIG, "20000");
        configurations.put(StreamsConfig.RETRY_BACKOFF_MS_CONFIG, "500");
        configurations.put(StreamsConfig.STATE_DIR_CONFIG, "/tmp/kafka-streams");

        return configurations;
    }
}
