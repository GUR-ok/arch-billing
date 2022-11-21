package ru.gur.archbilling.persistence;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.gur.archbilling.entity.Account;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Optional;
import java.util.UUID;

@Repository
@Profile("!hw09")
public interface AccountRepository extends JpaRepository<Account, UUID> {

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "FROM Account AS a WHERE a.id = :id")
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "5000")}) //ms
    Optional<Account> findByIdLocked(@Param("id") UUID id);
}
