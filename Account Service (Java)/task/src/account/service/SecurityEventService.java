package account.service;

import account.domain.SecurityEvent;
import account.domain.User;
import account.repository.SecurityEventsRepository;
import account.request.UpdateRoleRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class SecurityEventService {
    private final SecurityEventsRepository eventsRepository;
    private final HttpServletRequest request;

    public SecurityEventService(SecurityEventsRepository eventsRepository, HttpServletRequest request) {
        this.eventsRepository = eventsRepository;
        this.request = request;
    }


    public void createUserEvent(User user) {
        SecurityEvent securityEventInstance = new SecurityEvent();
        securityEventInstance.setDate(LocalDateTime.now());
        securityEventInstance.setEventName("CREATE_USER");
        securityEventInstance.setSubject("Anonymous");
        securityEventInstance.setObject(user.getEmail());
        securityEventInstance.setPath("/api/auth/signup");
        eventsRepository.save(securityEventInstance);
    }

    public void changePasswordEvent(User user) {
        SecurityEvent securityEventInstance = new SecurityEvent();
        securityEventInstance.setEventName("CHANGE_PASSWORD");
        securityEventInstance.setSubject(SecurityContextHolder.getContext().getAuthentication().getName());
        securityEventInstance.setObject(user.getEmail());
        securityEventInstance.setPath(request.getRequestURI());
        eventsRepository.save(securityEventInstance);
    }
    public void accessDeniedEvent() {
        SecurityEvent securityEventInstance = new SecurityEvent();
        securityEventInstance.setEventName("ACCESS_DENIED");
        securityEventInstance.setSubject(SecurityContextHolder.getContext().getAuthentication().getName());
        securityEventInstance.setObject(request.getRequestURI());
        securityEventInstance.setPath(request.getRequestURI());
        eventsRepository.save(securityEventInstance);
    }
    public void loginFailureEvent(String username) {
        SecurityEvent securityEventInstance = new SecurityEvent();
        securityEventInstance.setDate(LocalDateTime.now());
        securityEventInstance.setEventName("LOGIN_FAILED");
        securityEventInstance.setSubject(username);
        securityEventInstance.setObject(request.getRequestURI());
        securityEventInstance.setPath(request.getRequestURI());
        eventsRepository.save(securityEventInstance);
    }

    public void updateUserEvent(String operation, User user, UpdateRoleRequest roleRequest){
        SecurityEvent securityEventInstance = new SecurityEvent();
        if(operation.equals("GRANT")) {
            securityEventInstance.setDate(LocalDateTime.now());
            securityEventInstance.setEventName("GRANT_ROLE");
            securityEventInstance.setSubject(SecurityContextHolder.getContext().getAuthentication().getName());
            securityEventInstance.setObject("Grant role " + roleRequest.getRole() + " to " + user.getEmail());
            securityEventInstance.setPath("/api/admin/user/role");
        } else if(operation.equals("REMOVE")) {
            securityEventInstance.setDate(LocalDateTime.now());
            securityEventInstance.setEventName("REMOVE_ROLE");
            securityEventInstance.setSubject(SecurityContextHolder.getContext().getAuthentication().getName());
            securityEventInstance.setObject("Remove role " + roleRequest.getRole() + " from " + user.getEmail());
            securityEventInstance.setPath("/api/admin/user/role");
        }
        eventsRepository.save(securityEventInstance);
    }
    public void lockUserEvent(User user) {
        SecurityEvent securityEventInstance = new SecurityEvent();
        securityEventInstance.setDate(LocalDateTime.now());
        securityEventInstance.setEventName("LOCK_USER");
        securityEventInstance.setSubject(SecurityContextHolder.getContext().getAuthentication().getName());
        securityEventInstance.setObject("Lock user " + user.getEmail());
        securityEventInstance.setPath(request.getRequestURI());
        eventsRepository.save(securityEventInstance);
    }
    public void unlockUserEvent(User user) {
        SecurityEvent securityEventInstance = new SecurityEvent();
        securityEventInstance.setEventName("UNLOCK_USER");
        securityEventInstance.setSubject(SecurityContextHolder.getContext().getAuthentication().getName());
        securityEventInstance.setObject("Unlock user " + user.getEmail());
        securityEventInstance.setPath(request.getRequestURI());
        eventsRepository.save(securityEventInstance);
    }
    public void deleteUserEvent(String email) {
        SecurityEvent securityEventInstance = new SecurityEvent();
        securityEventInstance.setEventName("DELETE_USER");
        securityEventInstance.setSubject(SecurityContextHolder.getContext().getAuthentication().getName());
        securityEventInstance.setObject(email);
        securityEventInstance.setPath("/api/admin/user");
        eventsRepository.save(securityEventInstance);
    }
    public void bruteForceEvent(String username) {
        SecurityEvent securityEventInstance = new SecurityEvent();
        securityEventInstance.setDate(LocalDateTime.now());
        securityEventInstance.setEventName("BRUTE_FORCE");
        securityEventInstance.setSubject(username);
        securityEventInstance.setObject(request.getRequestURI());
        securityEventInstance.setPath(request.getRequestURI());
        eventsRepository.save(securityEventInstance);
    }
    public void lockUserAfterBruteForceEvent(String username) {
        SecurityEvent securityEventInstance = new SecurityEvent();
        securityEventInstance.setEventName("LOCK_USER");
        securityEventInstance.setSubject(username);
        securityEventInstance.setObject("Lock user " + username);
        securityEventInstance.setPath(request.getRequestURI());
        eventsRepository.save(securityEventInstance);
    }
}
