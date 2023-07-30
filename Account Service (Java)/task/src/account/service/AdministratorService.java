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
import account.response.DeleteUserResponse;
import account.response.UserResponse;
import org.springframework.http.HttpStatus;
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
    private final Mapper mapper;
    private Set<Role> businessRoles;
    private Set<Role> administrativeRoles;

    public AdministratorService(UserRepository userRepository, RoleRepository roleRepository, SecurityEventsRepository securityEventsRepository, Mapper mapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.securityEventsRepository = securityEventsRepository;
        this.mapper = mapper;
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
        registerDeleteEvent(email);
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
        if(!userRepository.existsByEmailIgnoreCase(email)){
            throw new CustomException(HttpStatus.NOT_FOUND,"Not Found","User not found!");
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
                if(chosenUser.getRoles().stream().anyMatch(businessRoles::contains) && administrativeRoles.contains(requestedRole)){
                    throw new CustomException(HttpStatus.BAD_REQUEST,"Bad Request","The user cannot combine administrative and business roles!");
                }else if(chosenUser.getRoles().stream().anyMatch(administrativeRoles::contains) && businessRoles.contains(requestedRole)){
                    throw new CustomException(HttpStatus.BAD_REQUEST,"Bad Request","The user cannot combine administrative and business roles!");
                }

                chosenUser.getRoles().add(requestedRole);
                registerUpdateEvent("GRANT",chosenUser,updateRoleRequest);
                break;
            case "REMOVE":
                if (!chosenUser.getRoles().contains(requestedRole)) {
                    throw new CustomException(HttpStatus.BAD_REQUEST, "Bad Request", "The user does not have a role!");
                } else if (requestedRole.getName().contains("ROLE_ADMINISTRATOR")) {
                    throw new CustomException(HttpStatus.BAD_REQUEST, "Bad Request", "Can't remove ADMINISTRATOR role!");
                } else if(chosenUser.getRoles().stream().count() == 1){
                    throw new CustomException(HttpStatus.BAD_REQUEST,"Bad Request","The user must have at least one role!");
                }
                chosenUser.getRoles().remove(requestedRole);
                registerUpdateEvent("REMOVE",chosenUser,updateRoleRequest);
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
    private void defineGroups(){
        administrativeRoles.add(roleRepository.findById(1L).orElseThrow(()-> new CustomException(HttpStatus.NOT_FOUND,"Not Found","Role Not Found")));
        businessRoles.add(roleRepository.findById(2L).orElseThrow(()-> new CustomException(HttpStatus.NOT_FOUND,"Not Found","Role Not Found")));
        businessRoles.add(roleRepository.findById(3L).orElseThrow(()-> new CustomException(HttpStatus.NOT_FOUND,"Not Found","Role Not Found")));
    }
    private void registerUpdateEvent(String operation,User user,UpdateRoleRequest roleRequest){
        SecurityEvent event = new SecurityEvent();
        if(operation.equals("GRANT")) {

            event.setEventName("GRANT_ROLE");
            event.setSubject(user.getEmail());
            event.setObject("Grant role " + roleRequest.getRole() + " to " + user.getEmail());
            event.setPath("/api/admin/user/role");
        } else if(operation.equals("REMOVE")) {

            event.setEventName("REMOVE_ROLE");
            event.setSubject(user.getEmail());
            event.setObject("Remove role " + roleRequest.getRole() + " from " + user.getEmail());
            event.setPath("/api/admin/user/role");
        }
        securityEventsRepository.save(event);
    }
    private void registerDeleteEvent(String email) {
        SecurityEvent event = new SecurityEvent();

        event.setEventName("DELETE_USER");
        event.setSubject(email);
        event.setObject(email);
        event.setPath("/api/admin/user");
    }




}
