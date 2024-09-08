package com.example.Rewards.Management.service;

import com.example.Rewards.Management.dto.request.CustomerRegistrationRequest;
import com.example.Rewards.Management.dto.request.TransferRequest;
import com.example.Rewards.Management.dto.response.BalanceResponse;
import com.example.Rewards.Management.dto.response.CustomerRegistrationResponse;
import com.example.Rewards.Management.dto.response.TransferResponse;
import com.example.Rewards.Management.exceptions.CustomerNotFound;
import com.example.Rewards.Management.exceptions.TransactionNotFoundException;
import com.example.Rewards.Management.models.CashbackTransaction;
import com.example.Rewards.Management.models.Customer;
import com.example.Rewards.Management.repository.CashbackTransactionRepository;
import com.example.Rewards.Management.repository.CustomerRepository;
import com.example.Rewards.Management.service.Email.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImp implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CashbackTransactionRepository cashbackTransactionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public CustomerRegistrationResponse registerCustomer(CustomerRegistrationRequest request) {
        if (customerRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }
        Customer customer = modelMapper.map(request, Customer.class);
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setAccountNumber(generateAccountNumber());
        customer.setTotalCashback(BigDecimal.ZERO);
        customerRepository.save(customer);
        sendVerificationEmail(customer);
        CustomerRegistrationResponse response = new CustomerRegistrationResponse();
        response.setMessage("Registration successful! Verification email sent.");
        return response;
    }

    @Override
    public TransferResponse transferCashback(TransferRequest transferRequest) {
        Customer fromCustomer = customerRepository.findByAccountNumber(transferRequest.getFromAccountNumber())
                .orElseThrow(() -> new CustomerNotFound("Customer not found with account number: " + transferRequest.getFromAccountNumber()));
        Customer toCustomer = customerRepository.findByAccountNumber(transferRequest.getToAccountNumber())
                .orElseThrow(() -> new CustomerNotFound("Customer not found with account number: " + transferRequest.getToAccountNumber()));
        if (fromCustomer.getTotalCashback().compareTo(transferRequest.getAmount()) < 0) {
            throw new TransactionNotFoundException("Insufficient balance for transfer");
        }
        fromCustomer.setTotalCashback(fromCustomer.getTotalCashback().subtract(transferRequest.getAmount()));
        toCustomer.setTotalCashback(toCustomer.getTotalCashback().add(transferRequest.getAmount()));
        customerRepository.save(fromCustomer);
        customerRepository.save(toCustomer);
        CashbackTransaction fromTransaction = CashbackTransaction.builder()
                .transactionId(UUID.randomUUID().toString())
                .transactionDate(LocalDate.now())
                .amountEarned(transferRequest.getAmount().negate())
                .description("Cashback transfer to account number " + toCustomer.getAccountNumber())
                .customer(fromCustomer)
                .build();
        cashbackTransactionRepository.save(fromTransaction);
        CashbackTransaction toTransaction = CashbackTransaction.builder()
                .transactionId(UUID.randomUUID().toString())
                .transactionDate(LocalDate.now())
                .amountEarned(transferRequest.getAmount())
                .description("Cashback transfer from account number " + fromCustomer.getAccountNumber())
                .customer(toCustomer)
                .build();
        cashbackTransactionRepository.save(toTransaction);
        TransferResponse response = new TransferResponse();
        response.setMessage("Transfer successful!");
        response.setTransactionId(fromTransaction.getTransactionId());
        response.setAmountTransferred(transferRequest.getAmount());
        return response;
    }

    @Override
    public List<CashbackTransaction> getCashbackTransactions(String accountNumber) {
        Customer customer = customerRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new CustomerNotFound("Customer not found with account number: " + accountNumber));
        return cashbackTransactionRepository.findByCustomer(customer);
    }

    @Override
    public BalanceResponse getAccountBalance(String accountNumber) {
        Customer customer = customerRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new CustomerNotFound("Customer not found with account number: " + accountNumber));
        BalanceResponse response = new BalanceResponse();
        response.setAccountNumber(customer.getAccountNumber());
        response.setBalance(customer.getTotalCashback());
        return response;
    }

    private String generateAccountNumber() {
        return String.valueOf((long) (Math.random() * 10000000000L));
    }

    private void sendVerificationEmail(Customer customer) {
        String subject = "Welcome to Rewards Management!";
        String message = String.format(
                """
                Dear %s %s,
                
                Welcome to our rewards program! Your account has been successfully created.
                Your account number is: %s.
                
                Enjoy your cashback rewards!
                
                Best regards,
                The Rewards Team
                """,
                customer.getFirstName(),
                customer.getLastName(),
                customer.getAccountNumber()
        );
        emailService.sendEmail(customer.getEmail(), subject, message);
    }
}
