package account.repository;

import account.domain.Payment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {

    @Override
    @Transactional
    <S extends Payment> List<S> saveAll(Iterable<S> entities);

    @Override
    @Transactional
    <S extends Payment> S save(S entity);

    Payment findByEmployeeIgnoreCaseAndPeriod(String employee, Date period);
    List<Payment> findAllByEmployeeAndPeriod(Date period,String employee);
    List<Payment> findAllByEmployeeIgnoreCaseOrderByPeriodDesc(String employee);

}
