package account.controller;

import account.domain.SecurityEvent;
import account.service.AuditorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/security")
public class SecurityEventsController {
    private final AuditorService auditorService;

    public SecurityEventsController(AuditorService auditorService) {
        this.auditorService = auditorService;
    }

    @GetMapping(path = "/events/")
    public ResponseEntity<List<SecurityEvent>> getSecurityEvents(){
        return ResponseEntity.ok(auditorService.getSecurityEvents());
    }
}
