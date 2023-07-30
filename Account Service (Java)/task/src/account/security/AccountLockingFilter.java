package account.security;

import account.service.AccountLockingService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AccountLockingFilter  extends OncePerRequestFilter {
    private final AccountLockingService accountLockingService;

    public AccountLockingFilter(AccountLockingService accountLockingService) {
        this.accountLockingService = accountLockingService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String username = request.getRemoteUser();

        if (username != null && accountLockingService.isAccountLocked(username)) {
            response.sendRedirect("/account-locked");

        }
        filterChain.doFilter(request,response);
    }
}
