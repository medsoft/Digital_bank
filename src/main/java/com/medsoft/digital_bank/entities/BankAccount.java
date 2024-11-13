package com.medsoft.digital_bank.entities;

import com.medsoft.digital_bank.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", length = 4)
@NoArgsConstructor @AllArgsConstructor
@Data @Getter @Setter
@ToString
public abstract class  BankAccount {
    @Id
    private String  id;
    private Date createdAt ;
    private double balance;
    @Enumerated(EnumType.STRING)
    private AccountStatus status ;
    @ManyToOne
    private Customer customer ;
    @OneToMany (mappedBy = "bankAccount", fetch = FetchType.LAZY)
    private List<AccountOperation> accountOperations ;
}
