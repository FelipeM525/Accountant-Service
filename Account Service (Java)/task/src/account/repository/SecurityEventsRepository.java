package account.repository;

import account.domain.SecurityEvent;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SecurityEventsRepository extends JpaRepository<SecurityEvent,Long> {

}
