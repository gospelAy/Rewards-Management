package com.example.Rewards.Management.service;

import com.example.Rewards.Management.dto.request.CustomerRegistrationRequest;
import com.example.Rewards.Management.dto.request.TransferRequest;
import com.example.Rewards.Management.dto.response.BalanceResponse;
import com.example.Rewards.Management.dto.response.CustomerRegistrationResponse;
import com.example.Rewards.Management.dto.response.TransferResponse;
import com.example.Rewards.Management.models.CashbackTransaction;

import java.util.List;

public interface CustomerService {
    CustomerRegistrationResponse registerCustomer(CustomerRegistrationRequest request);
    TransferResponse transferCashback(TransferRequest transferRequest);
    List<CashbackTransaction> getCashbackTransactions(String accountNumber);
    BalanceResponse getAccountBalance(String accountNumber);

}
