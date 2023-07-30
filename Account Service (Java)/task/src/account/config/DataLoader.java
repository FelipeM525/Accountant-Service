package account.config;

import account.domain.Role;
import account.repository.RoleRepository;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {
    private final RoleRepository roleRepository;

    public DataLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
        createRoles();
    }

    private void createRoles() {
        try{
            roleRepository.save(new Role("ROLE_ADMINISTRATOR"));
            roleRepository.save(new Role("ROLE_USER"));
            roleRepository.save(new Role("ROLE_ACCOUNTANT"));
            roleRepository.save(new Role("ROLE_AUDITOR"));
        } catch (Exception e){

        }
    }
}
