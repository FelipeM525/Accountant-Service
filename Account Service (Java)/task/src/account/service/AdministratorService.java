package account.service;

import account.domain.Role;
import account.domain.SecurityEvent;
import account.domain.User;
import account.exception.CustomException;
import account.mapper.Mapper;
import account.repository.RoleRepository;
import account.repository.SecurityEventsRepository;
import account.repository.UserRepository;
import account.request.UpdateRoleRequest;
import account.request.UpdateUserStatusRequest;
import account.response.DeleteUserResponse;
import account.response.UserResponse;
import ch.qos.logback.classic.spi.IThrowableProxy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdministratorService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SecurityEventsRepository securityEventsRepository;
    private final AccountLockingService lockingService;
    private final SecurityEventService eventService;
    private final Mapper mapper;
    private Set<Role> businessRoles;
    private Set<Role> administrativeRoles;

    public AdministratorService(UserRepository userRepository, RoleRepository roleRepository, SecurityEventsRepository securityEventsRepository, Mapper mapper, AccountLockingService lockingService, SecurityEventService eventService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.securityEventsRepository = securityEventsRepository;
        this.mapper = mapper;
        this.lockingService = lockingService;
        this.eventService = eventService;
        this.businessRoles = new HashSet<>();
        this.administrativeRoles = new HashSet<>();
    }

    public List<UserResponse> getAllUserInfo() {
        List<User> userInfo = userRepository.findAll();
        Comparator<User> userIdComparator = Comparator.comparing(User::getId);
        Collections.sort(userInfo, userIdComparator);
        return userInfo.stream().map(mapper::convertUserToUserResponse).collect(Collectors.toList());
    }

    @Transactional
    public DeleteUserResponse deleteUser(String email) {
        checkIfUserExists(email);
        checkIfUserIsAdmin(email);
        userRepository.deleteUserByEmailIgnoreCase(email);
        eventService.deleteUserEvent(email);
        DeleteUserResponse response = new DeleteUserResponse();
        response.setUser(email);
        return response;
    }

    private void checkIfUserIsAdmin(String email) {
        User user = userRepository.findByEmailIgnoreCase(email).get();
        if (user.getRoles().contains(roleRepository.findByNameIgnoreCase("ROLE_ADMINISTRATOR").get())) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Bad Request", "Can't remove ADMINISTRATOR role!");
        }
    }

    private void checkIfUserExists(String email) {
        if (!userRepository.existsByEmailIgnoreCase(email)) {
            throw new CustomException(HttpStatus.NOT_FOUND, "Not Found", "User not found!");
        }
    }

    @Transactional
    public UserResponse updateRole(UpdateRoleRequest updateRoleRequest) {
        defineGroups();
        String userEmailFromRequest = updateRoleRequest.getUser().toLowerCase();
        User chosenUser = checkIfUserExistsAndGet(userEmailFromRequest);
        Role requestedRole = checkIfRoleExistsAndGet(updateRoleRequest.getRole());
        String operation = updateRoleRequest.getOperation();

        switch (operation) {
            case "GRANT":
                if (chosenUser.getRoles().stream().anyMatch(businessRoles::contains) && administrativeRoles.contains(requestedRole)) {
                    throw new CustomException(HttpStatus.BAD_REQUEST, "Bad Request", "The user cannot combine administrative and business roles!");
                } else if (chosenUser.getRoles().stream().anyMatch(administrativeRoles::contains) && businessRoles.contains(requestedRole)) {
                    throw new CustomException(HttpStatus.BAD_REQUEST, "Bad Request", "The user cannot combine administrative and business roles!");
                }

                chosenUser.getRoles().add(requestedRole);
                eventService.updateUserEvent("GRANT", chosenUser, updateRoleRequest);
                break;
            case "REMOVE":
                if (!chosenUser.getRoles().contains(requestedRole)) {
                    throw new CustomException(HttpStatus.BAD_REQUEST, "Bad Request", "The user does not have a role!");
                } else if (requestedRole.getName().contains("ROLE_ADMINISTRATOR")) {
                    throw new CustomException(HttpStatus.BAD_REQUEST, "Bad Request", "Can't remove ADMINISTRATOR role!");
                } else if (chosenUser.getRoles().stream().count() == 1) {
                    throw new CustomException(HttpStatus.BAD_REQUEST, "Bad Request", "The user must have at least one role!");
                }
                chosenUser.getRoles().remove(requestedRole);
                eventService.updateUserEvent("REMOVE", chosenUser, updateRoleRequest);
                break;
            default:
                throw new CustomException(HttpStatus.BAD_REQUEST, "Bad Request", "Invalid operation!");
        }

        userRepository.save(chosenUser);
        return mapper.convertUserToUserResponse(chosenUser);
    }

    private User checkIfUserExistsAndGet(String userEmail) {
        return userRepository.findByEmailIgnoreCase(userEmail)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Not Found", "User not found!"));
    }

    private Role checkIfRoleExistsAndGet(String roleName) {
        Optional<Role> role = roleRepository.findByNameIgnoreCase(roleName);
        if (role.isPresent()) {
            return role.get();
        } else if (roleRepository.findByNameIgnoreCase("ROLE_" + roleName).isPresent()) {
            return roleRepository.findByNameIgnoreCase("ROLE_" + roleName).get();
        } else {
            throw new CustomException(HttpStatus.NOT_FOUND, "Not Found", "Role not found!");
        }
    }

    private void defineGroups() {
        administrativeRoles.add(roleRepository.findById(1L).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Not Found", "Role Not Found")));
        businessRoles.add(roleRepository.findById(2L).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Not Found", "Role Not Found")));
        businessRoles.add(roleRepository.findById(3L).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Not Found", "Role Not Found")));
        businessRoles.add(roleRepository.findById(4L).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Not Found", "Role Not Found")));
    }

    @Transactional
    public Map<String, String> changeUserStatus(UpdateUserStatusRequest userStatusRequest) {
        String operation = userStatusRequest.getOperation();
        Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(userStatusRequest.getUser());
         if(!userRepository.findByEmailIgnoreCase(userStatusRequest.getUser()).isPresent()) {
             throw new CustomException(HttpStatus.BAD_REQUEST,"Bad Request","User not found!");
         }
        User user = optionalUser.get();
         if(user.getRoles().contains(roleRepository.findByNameIgnoreCase("ROLE_ADMINISTRATOR").get())) {
             throw new CustomException(HttpStatus.BAD_REQUEST,"Bad Request","Can't lock the ADMINISTRATOR!");
         }
            switch (operation) {
                case "LOCK":
                    user.setAccountNonLocked(false);
                    userRepository.save(user);
                    eventService.lockUserEvent(user);
                    break;

                case "UNLOCK":
                    user.setAccountNonLocked(true);
                    user.setFailedAttempt(0);
                    userRepository.save(user);
                    eventService.unlockUserEvent(user);
                    break;
                default:
                    throw new CustomException(HttpStatus.BAD_REQUEST, "Bad Request", "Invalid operation!");
            }
            if(userStatusRequest.getOperation().equals("UNLOCK")) {
                Map<String, String> userStatusResponse = Map.of("status", "User " + userStatusRequest.getUser().toLowerCase() + " unlocked!");
                return userStatusResponse;
            }
            Map<String, String> userStatusResponse = Map.of("status", "User " + userStatusRequest.getUser().toLowerCase() + " locked!");
            return userStatusResponse;


    }

}
