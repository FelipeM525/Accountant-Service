package account.repository;

import account.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByNameIgnoreCase(String name);


    Boolean existsByName(String name);
}
