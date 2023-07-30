package account.domain;

import account.mapper.CustomDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"employee", "period"})})
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String employee;
//   @JsonDeserialize(using = CustomDateDeserializer.class)
    private Date period;
    private Long salary;

    public Payment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public Date getPeriod() {
        return period;
    }

    public void setPeriod(Date period) {
        this.period = period;
    }

    public Long getSalary() {
        return salary;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", employee='" + employee + '\'' +
                ", period=" + period +
                ", salary=" + salary +
                '}';
    }
}
