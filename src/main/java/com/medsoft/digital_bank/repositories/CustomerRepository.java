package com.medsoft.digital_bank.repositories;

import com.medsoft.digital_bank.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
