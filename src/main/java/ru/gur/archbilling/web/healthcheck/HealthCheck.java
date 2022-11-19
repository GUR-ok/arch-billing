package ru.gur.archbilling.web.healthcheck;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gur.archbilling.web.healthcheck.response.Health;

@RestController
@RequiredArgsConstructor
public class HealthCheck {

    @GetMapping("/health")
    public Health health() {

        return Health.builder()
                .status(Health.HealthStatus.OK)
                .build();
    }
}
