package com.medsoft.digital_bank.entities;

import com.medsoft.digital_bank.enums.OperationType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@NoArgsConstructor @AllArgsConstructor
@Data @Getter @Setter @ToString
public class AccountOperation {

    @Id  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date operationDate;
    private double amount ;
    @Enumerated(EnumType.STRING)
    private OperationType type  ;
    private String description ;
    @ManyToOne
    private BankAccount bankAccount;
}
