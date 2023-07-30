package account.exception;

import account.domain.User;
import account.repository.SecurityEventsRepository;
import account.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final UserRepository userRepository;
    private final SecurityEventsRepository securityEventsRepository;

    public CustomAuthenticationFailureHandler(UserRepository userRepository, SecurityEventsRepository securityEventsRepository) {
        this.userRepository = userRepository;
        this.securityEventsRepository = securityEventsRepository;
    }

    @Transactional
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String email = request.getRemoteUser();
        User user = userRepository.findByEmailIgnoreCase(email).get();
        if (user != null) {
            user.setFailedAttempts(user.getFailedAttempts() + 1);
            userRepository.save(user);
        }
    }
}
