package account.service;

import account.domain.Payment;
import account.domain.SecurityEvent;
import account.exception.CustomException;
import account.mapper.Mapper;
import account.repository.PaymentRepository;
import account.repository.SecurityEventsRepository;
import account.repository.UserRepository;
import account.request.PaymentRequest;
import account.response.PaymentResponse;
import account.request.UpdatePaymentRequest;
import account.response.UpdatePaymentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountantService {
    private final PaymentRepository paymentRepository;
    private final SecurityEventsRepository securityEventsRepository;
    private final UserRepository userRepository;
    private final Mapper mapper;
    private final SimpleDateFormat inputFormatter = new SimpleDateFormat("MM-yyyy");

    public AccountantService(PaymentRepository paymentRepository, SecurityEventsRepository securityEventsRepository, UserRepository userRepository, Mapper mapper) {
        this.paymentRepository = paymentRepository;
        this.securityEventsRepository = securityEventsRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    public PaymentResponse registerPayment(List<PaymentRequest> paymentRequestList) {
        List<Payment> paymentList = getValidPayments(paymentRequestList);
        paymentRepository.saveAll(paymentList);
        return new PaymentResponse();
    }

    public List<Payment> getValidPayments(List<PaymentRequest> paymentList) {
        List<Payment> filteredPayments = paymentList.stream().map(paymentRequest -> {
            if (paymentRequest.getSalary() < 0) {
                throw new CustomException(HttpStatus.BAD_REQUEST, "Bad Request", "The salary can't be negative!");
            } else if (paymentRequest.getEmployee() == null) {
                throw new CustomException(HttpStatus.BAD_REQUEST, "Bad Request", "employee can't be empty");
            }
            Payment paymentOutput = new Payment();
            paymentOutput.setEmployee(paymentRequest.getEmployee());
            paymentOutput.setSalary(paymentRequest.getSalary());
            paymentOutput.setPeriod(convertPeriodToDate(paymentRequest.getPeriod()));
            return paymentOutput;
        }).collect(Collectors.toList());
        return filteredPayments;
    }

    public UpdatePaymentResponse updatePayment(UpdatePaymentRequest paymentRequest) {
        Payment existingPayment = paymentRepository.findByEmployeeIgnoreCaseAndPeriod(
                paymentRequest.getEmployee(), paymentRequest.getPeriod()
        );

        if (existingPayment == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Bad Request", "The user does not exist! " + paymentRequest.toString());
        }

        existingPayment.setSalary(paymentRequest.getSalary());
        paymentRepository.save(existingPayment);

        return new UpdatePaymentResponse();
    }
    private Date convertPeriodToDate(String period) {
        try {
            inputFormatter.setLenient(false);
            return inputFormatter.parse(period);
        } catch (ParseException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST,"Bad Request","Wrong date!");
        }
    }



}
