package com.example.Rewards.Management.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BalanceResponse {
    private String accountNumber;
    private BigDecimal balance;
}