package com.medsoft.digital_bank.services;

import com.medsoft.digital_bank.Dtos.*;

import java.util.List;

public interface BankAccountService {

    CustomerDTO saveCustomer(CustomerDTO customerDTO);

    CurrentBankAccountDTO saveCurrentBankAccount(double balance , Long customerId, double overDraft);

    SavingBankAccountDTO saveSavingBankAccount(double balance , Long customerId, double interestRate);

    List<CustomerDTO> listCustomers();

    CustomerDTO getCustomer(Long customerId);

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void delete(Long customerId);

    BankAccountDTO getBankAccount(String accountId);

    void debit(String accountId, double amount, String description);

    void credit(String accountId, double amount, String description);

    void virement(String accountIdSource, double amount, String accountIdDestination,String description);




    List <BankAccountDTO> listBankAccount();

    List<AccountOperationDTO> accountHistory(String accountId);

    AccountHistoryDTO getAccountHistory(String accountId, int page, int size);
}
