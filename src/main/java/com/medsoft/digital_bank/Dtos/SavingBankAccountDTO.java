package com.medsoft.digital_bank.Dtos;

import com.medsoft.digital_bank.entities.AccountOperation;
import com.medsoft.digital_bank.entities.Customer;
import com.medsoft.digital_bank.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;


@Data
public  class SavingBankAccountDTO  extends  BankAccountDTO{

    private String  id;
    private Date createdAt ;
    private double balance;
    private AccountStatus status ;
    private CustomerDTO customerDTO ;
    private double interstRate  ;
}
