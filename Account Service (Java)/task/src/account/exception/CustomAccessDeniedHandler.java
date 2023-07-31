package account.exception;

import account.service.SecurityEventService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.cglib.core.Local;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final SecurityEventService eventService;

    public CustomAccessDeniedHandler(SecurityEventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        response.setContentType("application/json");

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);


        LocalDateTime timestamp = LocalDateTime.now();
        int status = HttpServletResponse.SC_FORBIDDEN;
        String error = "Forbidden";
        String message = "Access Denied!";
        String path = request.getRequestURI();

        String jsonResponse = String.format("{\"timestamp\": \"%s\", \"status\": %d, \"error\": \"%s\", \"message\": \"%s\", \"path\": \"%s\"}",
                timestamp, status, error, message, path);
        eventService.accessDeniedEvent();
        response.getWriter().write(jsonResponse);
    }
}
