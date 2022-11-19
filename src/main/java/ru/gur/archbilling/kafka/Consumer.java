package ru.gur.archbilling.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.gur.archbilling.kafka.event.EventSource;
import ru.gur.archbilling.kafka.event.KafkaEvent;
import ru.gur.archbilling.kafka.eventhandler.EventHandler;


import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Profile({"hw09","local"})
public class Consumer {

    private final Set<EventHandler<EventSource>> eventHandlers;

    @KafkaListener(topics = "notification", containerFactory = "kafkaListenerContainerFactoryString")
    public void listenGroupTopic1(String message) {
        log.info("Receive message: {}", message);

        final ObjectMapper objectMapper = new ObjectMapper();

        try {
            final KafkaEvent eventSource = objectMapper.readValue(message, KafkaEvent.class);
            log.info("EventSource: {}", eventSource);

            eventHandlers.stream()
                    .filter(eventSourceEventHandler -> eventSourceEventHandler.canHandle(eventSource))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Handler for eventsource not found"))
                    .handleEvent(eventSource);

        } catch (JsonProcessingException e) {
            log.error("Couldn't parse message: {}; exception: ", message, e);
        }
    }

    @KafkaListener(topics = "AccountsWithBalance", containerFactory = "kafkaListenerContainerFactoryDouble")
    public void listenGroupTopic2(@Payload Double msg,
                                  @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) UUID key) {
        log.info("AccountsWithBalance Receive message: {} {}", key, msg);
    }
}
