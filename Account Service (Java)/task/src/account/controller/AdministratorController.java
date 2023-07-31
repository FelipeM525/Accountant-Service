package account.controller;

import account.request.UpdateRoleRequest;
import account.request.UpdateUserStatusRequest;
import account.response.DeleteUserResponse;
import account.response.UserResponse;
import account.service.AdministratorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/admin")
public class AdministratorController {
    private final AdministratorService adminService;

    public AdministratorController(AdministratorService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/user/")
    public ResponseEntity<List<UserResponse>> getUserInfo(){
        return ResponseEntity.ok(adminService.getAllUserInfo());
    }
    @PutMapping(path = "/user/role")
    public ResponseEntity<UserResponse> updateRole(@RequestBody UpdateRoleRequest updateRoleRequest){
        System.out.println(updateRoleRequest.toString());
        return ResponseEntity.ok(adminService.updateRole(updateRoleRequest));
    }
    @PutMapping(path = "/user/access")
    public ResponseEntity<Map<String,String>> changeUserStatus(@RequestBody UpdateUserStatusRequest request) {
        return ResponseEntity.ok(adminService.changeUserStatus(request));
    }
    @DeleteMapping("/user/{email}")
    public ResponseEntity<DeleteUserResponse> deleteRole(@PathVariable String email){
        return  ResponseEntity.ok(adminService.deleteUser(email));
    }
}
