package account.config;

import account.exception.CustomException;
import account.repository.UserRepository;
import account.service.AccountLockingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;

@Configuration

public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
    private final AccountLockingService lockingService;
    private final UserRepository userRepository;
    private final HttpServletRequest request;


    public AuthenticationFailureListener(AccountLockingService lockingService, UserRepository userRepository, HttpServletRequest request) {
        this.lockingService = lockingService;
        this.userRepository = userRepository;
        this.request = request;
    }

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        final String username = event.getAuthentication().getName();
        if (username != null) {
            lockingService.loginFailure(username);
        }
    }
}
