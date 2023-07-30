package account.response;

import account.domain.Role;
import account.mapper.RoleNameSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.HashSet;
import java.util.Set;

public class UserResponse {
    private Integer id;
    private String name;
    private String lastname;
    private String email;
    @JsonSerialize(using = RoleNameSerializer.class)
    private Set<Role> roles = new HashSet<>();

    public UserResponse() {
    }

    public UserResponse(Integer id, String name, String lastname, String email, Set<Role> roles) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.email = email.toLowerCase();
        this.roles = roles;
    }

    public UserResponse(Integer id, String name, String lastname, String email) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.email = email.toLowerCase();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
