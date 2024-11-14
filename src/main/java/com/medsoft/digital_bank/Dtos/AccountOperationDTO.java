package com.medsoft.digital_bank.Dtos;

import com.medsoft.digital_bank.enums.OperationType;
import lombok.*;

import java.util.Date;

@Data
public class AccountOperationDTO {

    private Long id;
    private Date operationDate;
    private double amount ;
    private OperationType type  ;
    private String description ;

}
