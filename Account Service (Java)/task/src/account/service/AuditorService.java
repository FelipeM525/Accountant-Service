package account.service;

import account.domain.SecurityEvent;
import account.repository.SecurityEventsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditorService {
    private final SecurityEventsRepository securityEventsRepository;

    public AuditorService(SecurityEventsRepository securityEventsRepository) {
        this.securityEventsRepository = securityEventsRepository;
    }

    public List<SecurityEvent> getSecurityEvents() {
        return securityEventsRepository.findAll();
    }
}
