package com.medsoft.digital_bank.Dtos;

import com.medsoft.digital_bank.entities.AccountOperation;
import com.medsoft.digital_bank.entities.Customer;
import com.medsoft.digital_bank.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data

public  class BankAccountDTO {

    private String  id;
    private Date createdAt ;
    private double balance;
    private AccountStatus status ;
    private String type ;
    private Customer customer ;
    //private List<AccountOperation> accountOperations ;
}
