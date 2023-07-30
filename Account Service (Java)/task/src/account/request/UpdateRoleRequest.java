package account.request;

import account.domain.Role;

import java.util.Set;

public class UpdateRoleRequest {
    private String user;
    private String role;
    private String operation;

    public UpdateRoleRequest() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    @Override
    public String toString() {
        return "UpdateRoleRequest{" +
                "user='" + user + '\'' +
                ", role='" + role + '\'' +
                ", operation='" + operation + '\'' +
                '}';
    }
}
