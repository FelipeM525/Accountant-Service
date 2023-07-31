package account.controller;


import account.domain.SecurityEvent;
import account.domain.User;
import account.mapper.Mapper;
import account.request.ChangePasswordRequest;
import account.response.ChangePasswordResponse;
import account.response.UserResponse;
import account.service.AuditorService;
import account.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationService authService;

    private final Mapper mapper;

    public AuthenticationController(AuthenticationService authService, Mapper mapper) {
        this.authService = authService;
        this.mapper = mapper;
    }

    @PostMapping(path = "/signup")
    public ResponseEntity<UserResponse> registerUser(@RequestBody @Valid User user){
        return  ResponseEntity.ok(mapper.convertUserToUserResponse(authService.registerUser(user)));
    }
    @PostMapping(path ="/changepass")
    public ResponseEntity<ChangePasswordResponse> changePassword(@RequestBody @Valid ChangePasswordRequest passwordRequest){
        return ResponseEntity.ok(mapper.convertUserToChangePaswordResponse(authService.changePassword(passwordRequest)));
    }
    @GetMapping(path = "/find")
    public ResponseEntity<?> findByEmail(@RequestParam(required = false) String email) {
        return ResponseEntity.ok(authService.findByEmail(email));
    }
}
