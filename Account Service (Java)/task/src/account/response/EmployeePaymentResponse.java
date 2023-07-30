package account.response;

import account.mapper.CustomDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDate;
import java.util.Date;

public class EmployeePaymentResponse {
    private String name;
    private String lastname;
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date period;
    private String salary;

    public EmployeePaymentResponse() {
    }

    public EmployeePaymentResponse(String name, String lastname, Date period, String salary) {
        this.name = name;
        this.lastname = lastname;
        this.period = period;
        this.salary = salary;
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

    public Date getPeriod() {
        return period;
    }

    public void setPeriod(Date period) {
        this.period = period;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }
}
