package com.example.Rewards.Management.repository;

import com.example.Rewards.Management.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByAccountNumber(String accountNumber);

}
