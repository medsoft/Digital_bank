package com.medsoft.digital_bank.services;

import com.medsoft.digital_bank.Dtos.*;
import com.medsoft.digital_bank.Exceptions.BankAccountNotFoundException;
import com.medsoft.digital_bank.Exceptions.CustomerNotFoundException;
import com.medsoft.digital_bank.entities.*;
import com.medsoft.digital_bank.enums.OperationType;
import com.medsoft.digital_bank.Exceptions.BalanceNotSufficientException;
import com.medsoft.digital_bank.mappers.BankAccountMapperImpl;
import com.medsoft.digital_bank.repositories.AccountOperationRepository;
import com.medsoft.digital_bank.repositories.BankAccountRepository;
import com.medsoft.digital_bank.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    private BankAccountMapperImpl dtoMapper ;

    // Traitement Metier

    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Save Customer");
        Customer customer =  dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer =  customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }
    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double balance, Long customerId, double overDraft) {
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
            return  dtoMapper.fromCurrentBankAccount(savedCurrentAccount);
    }
    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double balance, Long customerId, double interestRate) {
        SavingAccount savingAccount = new SavingAccount();
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
        return  dtoMapper.fromSavinBankAccount(savedSavingAccount);
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        List <Customer> customers =   customerRepository.findAll();
        List <CustomerDTO> customerDTOS = customers.stream().map(customer -> dtoMapper.fromCustomer(customer)).collect(Collectors.toList())  ;
        return customerDTOS ;
    }
    @Override
    public CustomerDTO getCustomer(Long customerId) {
         Customer customer =  customerRepository.findById(customerId).orElseThrow();
        return dtoMapper.fromCustomer(customer) ;
    }
    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("Save Customer");
        Customer customer =  dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer =  customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }
    @Override
    public void delete(Long customerId)
    {
        customerRepository.deleteById(customerId);
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(()->new BankAccountNotFoundException("BankAccount not found "));
        if (bankAccount instanceof SavingAccount) {
            SavingAccount savingAccount = (SavingAccount) bankAccount;
            return dtoMapper.fromSavinBankAccount(savingAccount) ;
        }else {
            CurrentAccount currentAccount = (CurrentAccount) bankAccount;
            return dtoMapper.fromCurrentBankAccount(currentAccount) ;
        }

    }

    // les methodes credit debit et virement pas besoin d'utiliser les Dto
    // ces methodes ne retournent rien et les parametres sont simples

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
    public List <BankAccountDTO> listBankAccount(){
        List <BankAccount> bankAccounts =  bankAccountRepository.findAll();
        List  <BankAccountDTO> bankAccountDTO =  bankAccounts.stream().map(bankAccount -> {
            if (bankAccount instanceof SavingAccount) {
                SavingAccount savingAccount = (SavingAccount) bankAccount;
                return dtoMapper.fromSavinBankAccount(savingAccount) ;
            }else {
                CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                return dtoMapper.fromCurrentBankAccount(currentAccount) ;
            }
        }).collect(Collectors.toList()) ;
        return bankAccountDTO ;
    }
    @Override
    public List<AccountOperationDTO> accountHistory(String accountId){
        List <AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId);
        return accountOperations.stream().map(op->
                dtoMapper.fromAccountOperation(op)).collect(Collectors.toList()) ;

    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId , int page, int size)
    {
        BankAccount bankAccount =  bankAccountRepository.findById(accountId).orElse(null);
        if (bankAccount == null) {
            throw new BankAccountNotFoundException("BankAccount not Found ");
        }
        Page <AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId,PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        List <AccountOperationDTO> accountOperationsDTO  =  accountOperations.getContent().stream().map(op->dtoMapper.fromAccountOperation(op)).collect(Collectors.toList()) ;
        accountHistoryDTO.setAccountOperationDtos(accountOperationsDTO);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPage(accountHistoryDTO.getTotalPage());
        return accountHistoryDTO ;
    }
}
