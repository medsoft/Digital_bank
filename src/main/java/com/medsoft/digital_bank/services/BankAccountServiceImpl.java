package com.medsoft.digital_bank.services;

import com.medsoft.digital_bank.Exceptions.BankAccountNotFoundException;
import com.medsoft.digital_bank.Exceptions.CustomerNotFoundException;
import com.medsoft.digital_bank.entities.*;
import com.medsoft.digital_bank.enums.OperationType;
import com.medsoft.digital_bank.Exceptions.BalanceNotSufficientException;
import com.medsoft.digital_bank.repositories.AccountOperationRepository;
import com.medsoft.digital_bank.repositories.BankAccountRepository;
import com.medsoft.digital_bank.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
//toujours utiliser @Transactional dans un service
@Transactional
//permet d'avoir un constructeur et en mme temps de faire l'njection des dependances
@AllArgsConstructor
//@Slf4j pour logger les messages ou encore log4j pour la journalisation
@Slf4j
public class BankAccountServiceImpl implements BankAccountService{

    // on peut utiliser @autowired pour faire l'injection des dependances mais il est deprecated
    //donc on va utiliser le constructeur a la place

    private CustomerRepository customerRepository ;
    private BankAccountRepository bankAccountRepository ;
    private AccountOperationRepository accountOperationRepository ;


    // Traitement Metier

    public Customer saveCustomer(Customer customer) {
        log.info("Save Customer");
        customer =  customerRepository.save(customer);
        return customer;
    }

    @Override
    public CurrentAccount saveCurrentBankAccount(double balance, Long customerId, double overDraft) {
        CurrentAccount currentAccount = new CurrentAccount(); ;
        //essayer cette methode plus tard
        customerRepository.findById(customerId).ifPresent(customer -> {}) ;
        Customer customer =  customerRepository.findById(customerId).get();
        if (customer == null ) {
            throw new CustomerNotFoundException("Customer not Found ");
        }
            currentAccount.setId(UUID.randomUUID().toString());
            currentAccount.setCreatedAt(new Date());
            currentAccount.setBalance(balance);
            currentAccount.setOverDraft(overDraft);
            currentAccount.setCustomer(customer);
            CurrentAccount savedCurrentAccount  = bankAccountRepository.save(currentAccount);
            return  savedCurrentAccount;

    }

    @Override
    public SavingAccount saveSavingBankAccount(double balance, Long customerId, double interestRate) {
        SavingAccount savingAccount = new SavingAccount(); ;
        //essayer cette methode plus tard
        customerRepository.findById(customerId).ifPresent(customer -> {}) ;
        Customer customer =  customerRepository.findById(customerId).get();
        if (customer == null ) {
            throw new CustomerNotFoundException("Customer not Found ");
        }
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(balance);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCustomer(customer);
        SavingAccount  savedSavingAccount  = bankAccountRepository.save(savingAccount);
        return  savedSavingAccount;
    }

    @Override
    public List<Customer> listCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public BankAccount getBankAccountById(String accountId) {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElse(null);
        if (bankAccount == null) {
            throw new BankAccountNotFoundException("BankAccount not Found ");
        }
        return bankAccount;
    }

    @Override
    public void debit(String accountId, double amount, String description) {
        BankAccount bankAccount =  bankAccountRepository.findById(accountId).orElse(null);

        if (bankAccount == null) {
            throw new BankAccountNotFoundException("BankAccount not Found ");
        }
        if (bankAccount.getBalance() <= amount) {
            throw new BalanceNotSufficientException("Debit Account not Found ");
        }else {
            AccountOperation accountOperation = new AccountOperation();
            accountOperation.setType(OperationType.DEBIT);
            accountOperation.setAmount(amount);
            accountOperation.setDescription(description);
            accountOperation.setBankAccount(bankAccount);
            accountOperationRepository.save(accountOperation);
            bankAccount.setBalance(bankAccount.getBalance() - amount);
            bankAccountRepository.save(bankAccount);
        }

    }

    @Override
    public void credit(String accountId, double amount, String description) {
        BankAccount bankAccount =  bankAccountRepository.findById(accountId).orElse(null);
        if (bankAccount == null) {
            throw new BankAccountNotFoundException("BankAccount not Found ");
        }
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount) ;
    }

    @Override
    public void virement(String accountIdSource, double amount, String accountIdDestination, String description) {

        debit(accountIdSource, amount , "Trnsfer From" );
        credit(accountIdDestination, amount, "Trnsfer To" );
    }
    @Override
    public List <BankAccount> bankAccountList(){
        return bankAccountRepository.findAll();
    }
}
