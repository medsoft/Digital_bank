package com.medsoft.digital_bank.repositories;

import com.medsoft.digital_bank.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
}
