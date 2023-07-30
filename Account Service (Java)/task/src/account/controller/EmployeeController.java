package account.controller;

import account.domain.User;
import account.mapper.Mapper;
import account.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/empl")
public class EmployeeController {
    private final PaymentService payService;
    private final Mapper mapper;

    public EmployeeController(PaymentService payService, Mapper mapper) {
        this.payService = payService;
        this.mapper = mapper;
    }

    @GetMapping(path = "/payment")
    public ResponseEntity<?> payment(@RequestParam("period")@Nullable String period, Authentication auth){
        User currentUser = (User) auth.getPrincipal();
        if(period == null){
            return ResponseEntity.ok(payService.findAllPayments(currentUser.getEmail()));
        }
        System.out.println(period);
        System.out.println(mapper.convertPeriodStringToDate(period).toString());
        return ResponseEntity.ok(payService.findPaymentByPeriod(mapper.convertPeriodStringToDate(period),currentUser.getEmail()));

    }


}
