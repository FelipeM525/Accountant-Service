package account.repository;

import account.domain.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    @Override
    @Transactional
    <S extends User> List<S> saveAll(Iterable<S> entities);

    @Override
    @Transactional
    <S extends User> S save(S entity);
    Optional<User> findByEmailIgnoreCase(String email);
    void deleteUserByEmailIgnoreCase(String email);
    Boolean existsByEmailIgnoreCase(String email);
    List<User> findAll();



}
