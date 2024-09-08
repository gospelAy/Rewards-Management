package com.example.Rewards.Management.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferResponse {
    private String message;
    private String transactionId;
    private BigDecimal amountTransferred;
}