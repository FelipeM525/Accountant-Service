package account.controller;

import account.request.PaymentRequest;
import account.response.PaymentResponse;
import account.request.UpdatePaymentRequest;
import account.response.UpdatePaymentResponse;
import account.service.AccountantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/acct")
public class AccountantController {
    private final AccountantService acctService;

    public AccountantController(AccountantService acctService) {
        this.acctService = acctService;
    }

    @PostMapping(path = "/payments")
    public ResponseEntity<PaymentResponse> registerSalary(@RequestBody List<PaymentRequest> paymentRequestList) {
        return ResponseEntity.ok(acctService.registerPayment(paymentRequestList));
    }

    @PutMapping(path = "/payments")
    public ResponseEntity<UpdatePaymentResponse> changeSalary(@RequestBody UpdatePaymentRequest paymentRequest) {
        return ResponseEntity.ok(acctService.updatePayment(paymentRequest));
    }
}
