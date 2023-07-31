package account.config;

import account.service.AccountLockingService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
@Configuration
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {
    private final AccountLockingService lockingService;

    public AuthenticationSuccessEventListener(AccountLockingService lockingService) {
        this.lockingService = lockingService;
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        final String username = event.getAuthentication().getName();
        if(username != null) {
            lockingService.loginSuccess(username);
        }
    }
}
