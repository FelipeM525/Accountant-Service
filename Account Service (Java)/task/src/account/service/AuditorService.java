package account.service;

import account.domain.SecurityEvent;
import account.repository.SecurityEventsRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditorService {
    private final SecurityEventsRepository securityEventsRepository;

    public AuditorService(SecurityEventsRepository securityEventsRepository) {
        this.securityEventsRepository = securityEventsRepository;
    }

    public List<SecurityEvent> getSecurityEvents() {
        Sort sort = Sort.by(Sort.Direction.ASC,"id");
        return securityEventsRepository.findAll(sort);
    }
}
