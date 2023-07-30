package account.service;

import account.domain.User;
import account.repository.SecurityEventsRepository;
import account.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountLockingService {
    private final UserRepository userRepository;
    private final SecurityEventsRepository securityEventsRepository;

    public AccountLockingService(UserRepository userRepository, SecurityEventsRepository securityEventsRepository) {
        this.userRepository = userRepository;
        this.securityEventsRepository = securityEventsRepository;
    }
    public Boolean isAccountLocked(String username) {
        User user = userRepository.findByEmailIgnoreCase(username).get();
        return user != null && user.isLocked();
    }
    @Transactional
    public void lockAccount(String username) {
        User user = userRepository.findByEmailIgnoreCase(username).get();
        if (user != null) {
            user.setLocked(true);
            userRepository.save(user);
        }
    }
}
