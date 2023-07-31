package account.service;

import account.domain.SecurityEvent;
import account.domain.User;
import account.exception.CustomException;
import account.repository.RoleRepository;
import account.repository.SecurityEventsRepository;
import account.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountLockingService {
    private final UserRepository userRepository;
    private final SecurityEventsRepository securityEventsRepository;
    private final SecurityEventService eventService;
    private final RoleRepository roleRepository;

    public AccountLockingService(UserRepository userRepository, SecurityEventsRepository securityEventsRepository, SecurityEventService eventService, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.securityEventsRepository = securityEventsRepository;
        this.eventService = eventService;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public void loginSuccess(String email) {
        User user = userRepository.findByEmailIgnoreCase(email).orElseGet(User::new);
            user.setFailedAttempt(0);
            userRepository.save(user);
    }
    @Transactional
    public void loginFailure(String email) {
        if(userRepository.existsByEmailIgnoreCase(email)) {
            User user = userRepository.findByEmailIgnoreCase(email).get();
            user.setFailedAttempt(user.getFailedAttempt() + 1);
            eventService.loginFailureEvent(email);
            lockAccount(user.getEmail());
            userRepository.save(user);
        } else {
            eventService.loginFailureEvent(email);
        }

    }
    public Boolean isAccountLocked(String username) {
        User user = userRepository.findByEmailIgnoreCase(username).get();
        return user != null && !user.isAccountNonLocked();
    }
    @Transactional
    public void lockAccount(String username) {
        User user = userRepository.findByEmailIgnoreCase(username).get();
        checkIfUserIsAdmin(user);
        if (user != null && user.getFailedAttempt() > 4) {
            user.setAccountNonLocked(false);
            userRepository.save(user);
            eventService.bruteForceEvent(username);
            eventService.lockUserAfterBruteForceEvent(username);


        }
    }
    private void checkIfUserIsAdmin(User user) {
        if(user.getRoles().contains(roleRepository.findByNameIgnoreCase("ROLE_ADMINISTRATOR").get())) {
            throw new CustomException(HttpStatus.BAD_REQUEST,"Bad Request","Can't lock the ADMINISTRATOR!");
        }
    }

}
