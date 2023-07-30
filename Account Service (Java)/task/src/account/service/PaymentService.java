package account.service;

import account.domain.Payment;
import account.domain.User;
import account.exception.CustomException;
import account.mapper.Mapper;
import account.repository.PaymentRepository;
import account.repository.UserRepository;
import account.response.EmployeePaymentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final Mapper mapper;


    public PaymentService(UserRepository userRepository, PaymentRepository paymentRepository, Mapper mapper) {
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
        this.mapper = mapper;
    }
    public EmployeePaymentResponse findPaymentByPeriod(Date period, String employee) {
        Payment payment = paymentRepository.findByEmployeeIgnoreCaseAndPeriod(employee,period);
        checkIfPaymentIsNull(payment);
        User user = userRepository.findByEmailIgnoreCase(payment.getEmployee()).get();
        EmployeePaymentResponse paymentResponse = mapper.converPaymentToEmployeePaymentResponse(payment);
        String formattedSalary = String.format("%d dollar(s) %d cent(s)", payment.getSalary() / 100, payment.getSalary() % 100);
    paymentResponse.setName(user.getName());
    paymentResponse.setLastname(user.getLastname());
    paymentResponse.setSalary(formattedSalary);
    return paymentResponse;

    }

    public List<EmployeePaymentResponse> findAllPayments(String employee) {
        return paymentRepository.findAllByEmployeeIgnoreCaseOrderByPeriodDesc(employee).stream().map(payment ->{
            EmployeePaymentResponse paymentResponse = new EmployeePaymentResponse();
            checkIfPaymentIsNull(payment);
            User user = userRepository.findByEmailIgnoreCase(payment.getEmployee()).get();
            paymentResponse.setName(user.getName());
            paymentResponse.setLastname(user.getLastname());
            String formattedSalary = String.format("%d dollar(s) %d cent(s)", payment.getSalary() / 100, payment.getSalary() % 100);
            paymentResponse.setSalary(formattedSalary);
            paymentResponse.setPeriod(payment.getPeriod());
            return paymentResponse;
        }).collect(Collectors.toList());
    }
    private void checkIfPaymentIsNull(Payment payment){
        if(payment == null){
            throw new CustomException(HttpStatus.BAD_REQUEST,"Bad Request","payment is null");
        }
    }


}


