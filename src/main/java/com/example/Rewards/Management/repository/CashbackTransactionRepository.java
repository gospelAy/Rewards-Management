package com.example.Rewards.Management.repository;

import com.example.Rewards.Management.models.CashbackTransaction;
import com.example.Rewards.Management.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CashbackTransactionRepository extends JpaRepository<CashbackTransaction, Long> {
    List<CashbackTransaction> findByCustomer(Customer customer);
}
