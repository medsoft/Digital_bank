package com.medsoft.digital_bank.entities;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor @AllArgsConstructor
@DiscriminatorValue("CA")
@Data
public class CurrentAccount extends BankAccount{

    private double overDraft ;
}
