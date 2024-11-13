package com.medsoft.digital_bank.services;

import com.medsoft.digital_bank.entities.BankAccount;
import com.medsoft.digital_bank.entities.CurrentAccount;
import com.medsoft.digital_bank.entities.Customer;
import com.medsoft.digital_bank.entities.SavingAccount;

import java.util.List;

public interface BankAccountService {

    Customer saveCustomer(Customer customer);

    CurrentAccount saveCurrentBankAccount(double balance , Long customerId, double overDraft);
    SavingAccount  saveSavingBankAccount(double balance , Long customerId, double interestRate);

    List<Customer> listCustomers();
    BankAccount getBankAccountById(String accountId);

    void debit(String accountId, double amount, String description);
    void credit(String accountId, double amount, String description);
    void virement(String accountIdSource, double amount, String accountIdDestination,String description);

    List <BankAccount> bankAccountList();
}
