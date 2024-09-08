package com.example.Rewards.Management.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CustomerRegistrationRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String gender;
    private String address;
    private String phoneNumber;
    private String accountNumber;
    private BigDecimal totalCashback;
}
