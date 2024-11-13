package com.medsoft.digital_bank;

import com.medsoft.digital_bank.entities.AccountOperation;
import com.medsoft.digital_bank.entities.CurrentAccount;
import com.medsoft.digital_bank.entities.Customer;
import com.medsoft.digital_bank.entities.SavingAccount;
import com.medsoft.digital_bank.enums.AccountStatus;
import com.medsoft.digital_bank.enums.OperationType;
import com.medsoft.digital_bank.repositories.AccountOperationRepository;
import com.medsoft.digital_bank.repositories.BankAccountRepository;
import com.medsoft.digital_bank.repositories.CustomerRepository;
import com.medsoft.digital_bank.services.BankAccountService;
import com.medsoft.digital_bank.services.BankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class DigitalBankApplication {

    private final BankService bankService;

    public DigitalBankApplication(BankService bankService) {
        this.bankService = bankService;
    }

    public static void main(String[] args) {
        SpringApplication.run(DigitalBankApplication.class, args);
    }

    //@Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService) {
        return args -> {
            Stream.of("Ahmad", "Abass", "Ibrahim", "Khadija").forEach(name -> {

                Customer customer =  new Customer() ;
                bankAccountService.saveCustomer(customer) ;
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                bankAccountService.saveCustomer(customer) ;
            });
                bankAccountService.listCustomers().forEach(customer -> {
                bankAccountService.saveCurrentBankAccount(Math.random()*9000, customer.getId(), 5000);
                bankAccountService.saveSavingBankAccount(Math.random()*9000, customer.getId(), 3.5);

                bankAccountService.bankAccountList().forEach(account -> {

                    for (int i = 0; i < 4 ; i++) {

                        bankAccountService.credit(account.getId() ,  10000+Math.random()*500 ,"Compte Credité");
                        bankAccountService.debit(account.getId() , Math.random()*1000, "Compte débité");
                    }
                });

            });

        };
    }

    //@Bean
    CommandLineRunner start(CustomerRepository customerRepository ,
                            BankAccountRepository bankAccountRepository,
                            AccountOperationRepository accountOperationRepository) {
        return args ->{

            Stream.of("Ahmad", "Abass","Ibrahim","Khadija").forEach(name->{
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                customerRepository.save(customer);
            });


                customerRepository.findAll().forEach(cust->{
                CurrentAccount currentAccount =    new CurrentAccount() ;
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random()*90000);
                currentAccount.setCreatedAt(new Date());
                currentAccount.setOverDraft(9000);
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(cust);
                bankAccountRepository.save(currentAccount);
            });
                customerRepository.findAll().forEach(customer -> {
                    SavingAccount  savingAccount = new SavingAccount();
                    savingAccount.setId(UUID.randomUUID().toString());
                    savingAccount.setBalance(Math.random()*12000);
                    savingAccount.setCreatedAt(new Date());
                    savingAccount.setCustomer(customer);
                    savingAccount.setStatus(AccountStatus.CREATED);
                    savingAccount.setInterestRate(3.5);
                    bankAccountRepository.save(savingAccount);
                });


            bankAccountRepository.findAll().forEach(acc->{
                for (int i = 0; i < 5  ; i++) {

                    AccountOperation accountOperation = new AccountOperation();
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setAmount(Math.random() * 4000);
                    accountOperation.setType(Math.random() > 0.5 ? OperationType.DEBIT : OperationType.CREDIT);
                    accountOperation.setBankAccount(acc);
                    accountOperationRepository.save(accountOperation);

                }

                });



        };


    }

}
