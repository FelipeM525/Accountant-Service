package account.request;

import account.mapper.CustomDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Date;

public class UpdatePaymentRequest {
    private String employee;
    @JsonDeserialize(using = CustomDateDeserializer.class)
    private Date period;
    private Long salary;

    public String getEmployee() {
        return employee;
    }

    public Date getPeriod() {
        return period;
    }

    public Long getSalary() {
        return salary;
    }

    @Override
    public String toString() {
        return "UpdatePaymentRequest{" +
                "employee='" + employee + '\'' +
                ", period=" + period +
                ", salary=" + salary +
                '}';
    }
}
