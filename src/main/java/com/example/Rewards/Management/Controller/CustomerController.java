package com.example.Rewards.Management.Controller;

import com.example.Rewards.Management.dto.request.CustomerRegistrationRequest;
import com.example.Rewards.Management.dto.request.LoginRequest;
import com.example.Rewards.Management.dto.request.TransferRequest;
import com.example.Rewards.Management.dto.response.BalanceResponse;
import com.example.Rewards.Management.dto.response.CustomerRegistrationResponse;
import com.example.Rewards.Management.dto.response.TransferResponse;
import com.example.Rewards.Management.exceptions.CustomerNotFound;
import com.example.Rewards.Management.models.CashbackTransaction;
import com.example.Rewards.Management.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.Rewards.Management.Utils.AppUtils.MESSAGE_FOR_LOGIN_RESPONSE;

@RestController
@RequestMapping("/api/rewards")
@AllArgsConstructor
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    private final AuthenticationManager authenticationManager;


    @PostMapping("/register")
    public ResponseEntity<CustomerRegistrationResponse> registerCustomer(@RequestBody CustomerRegistrationRequest request) {
        CustomerRegistrationResponse response = customerService.registerCustomer(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        if (authentication.isAuthenticated()) {
            return new ResponseEntity<>(MESSAGE_FOR_LOGIN_RESPONSE, HttpStatus.OK);
        } else {
            throw new CustomerNotFound("The user is not found");
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transferCashback(@RequestBody TransferRequest request) {
        TransferResponse response = customerService.transferCashback(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/balance/{accountNumber}")
    public ResponseEntity<BalanceResponse> getAccountBalance(@PathVariable String accountNumber) {
        BalanceResponse response = customerService.getAccountBalance(accountNumber);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/history/{accountNumber}")
    public ResponseEntity<List<CashbackTransaction>> getCashbackTransactions(@PathVariable String accountNumber) {
        List<CashbackTransaction> transactions = customerService.getCashbackTransactions(accountNumber);
        return ResponseEntity.ok(transactions);
    }
}
