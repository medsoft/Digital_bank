package com.medsoft.digital_bank.web;


import com.medsoft.digital_bank.Dtos.AccountHistoryDTO;
import com.medsoft.digital_bank.Dtos.AccountOperationDTO;
import com.medsoft.digital_bank.Dtos.BankAccountDTO;
import com.medsoft.digital_bank.services.BankAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController

@Slf4j
public class BankAccountRestController {

    private BankAccountService bankAccountService;

    public BankAccountRestController(BankAccountService bankAccountService) {
        this.bankAccountService  =  bankAccountService;
    }

    @GetMapping("/bankAccount/{account_id}")
    public BankAccountDTO getBankAccountDTO (@PathVariable String  account_id) {
        return  bankAccountService.getBankAccount(account_id) ;
    }

    //public saveBankAccount
    @GetMapping("/listBankAccount")
    public List <BankAccountDTO> listBankAccount(){
       return  bankAccountService.listBankAccount() ;
    }

    @GetMapping("/accoount/{account_id}/operations")
    public List<AccountOperationDTO> getHistory(@PathVariable String  account_id) {
        return bankAccountService.accountHistory(account_id) ;
    }

    @GetMapping("/accoount/{account_id}/history")
    public AccountHistoryDTO getAccountHistory(@PathVariable String  account_id,
                                                     @RequestParam(name = "page", defaultValue = "0")int page,
                                                     @RequestParam(name = "size" , defaultValue = "5")int size) {
        return bankAccountService.getAccountHistory(account_id, page , size) ;
    }
}
