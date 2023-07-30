package account.repository;

import account.domain.SecurityEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecurityEventsRepository extends JpaRepository<SecurityEvent,Long> {

}
