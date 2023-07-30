package account.service;

import account.domain.SecurityEvent;
import account.domain.User;
import account.exception.CustomException;
import account.repository.RoleRepository;
import account.repository.SecurityEventsRepository;
import account.repository.UserRepository;
import account.request.ChangePasswordRequest;
import account.mapper.Mapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Set;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SecurityEventsRepository securityEventsRepository;
    private final PasswordEncoder passwordEncoder;
    private final Mapper mapper;

    public AuthenticationService(UserRepository userRepository, RoleRepository roleRepository, SecurityEventsRepository securityEventsRepository, PasswordEncoder passwordEncoder, Mapper mapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.securityEventsRepository = securityEventsRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

@Transactional
    public User registerUser(User user) {
        if (findByEmail(user.getEmail()).isPresent()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Bad Request", "User exist!");
        }
        user.setEmail(user.getEmail().toLowerCase());
        passwordSafetyChecker(user);
        saveUser(user);
        registerEvent(user);
        return user;
    }
    private void passwordSafetyChecker(User user){
        Set<String> breachedPasswords = Set.of("PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch", "PasswordForApril",
                "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust",
                "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember");
        if(breachedPasswords.contains(user.getPassword())){
            throw new CustomException(HttpStatus.BAD_REQUEST,"Bad Request","The password is in the hacker's database!");
        }
        if(user.getPassword().length() < 12){
            throw new CustomException(HttpStatus.BAD_REQUEST,"Bad Request","Password length must be 12 chars minimum!");
        }
    }

    public void saveUser(User user) {
        checkIfEmailIsValid(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if(userRepository.count() == 0){
            user.getRoles().add(roleRepository.findByNameIgnoreCase("ROLE_ADMINISTRATOR").get());
            userRepository.save(user);
        }else{
        user.getRoles().add(roleRepository.findByNameIgnoreCase("ROLE_USER").get());
        userRepository.save(user);
        }
    }

    private void checkIfEmailIsValid(User user) {
        if (!user.getEmail().matches("^[A-Za-z0-9._%+-]+@acme\\.com$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }


    @Transactional
    public User changePassword(ChangePasswordRequest passwordRequest) {
        User userForPasswordChecking = new User();
        userForPasswordChecking.setPassword(passwordRequest.getNewPassword());
        passwordSafetyChecker(userForPasswordChecking);
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        checkIfPasswordsAreTheSame(passwordRequest,currentUser);
        User updatedUser = userRepository.save(currentUser);
        return updatedUser;
    }
    private void checkIfPasswordsAreTheSame(ChangePasswordRequest passwordRequest,User currentUser){
        if (passwordEncoder.matches(passwordRequest.getNewPassword(),currentUser.getPassword())) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Bad Request", "The passwords must be different!");
        }
        currentUser.setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));
    }

    private void registerEvent(User user){
        SecurityEvent createUser = new SecurityEvent("CREATE_USER", "Anonymous", user.getEmail(),"/api/auth/signup");
        securityEventsRepository.save(createUser);

    }

}
