package com.medsoft.digital_bank.repositories;

import com.medsoft.digital_bank.entities.AccountOperation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {

}
