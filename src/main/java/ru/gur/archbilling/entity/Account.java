package ru.gur.archbilling.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "uuid", name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @UpdateTimestamp
    @Column(name = "created", nullable = false, updatable = false)
    private LocalDateTime updated;

    @CreationTimestamp
    @Column(name = "created", insertable = false, nullable = false, updatable = false)
    private LocalDateTime created;
}
