package com.medsoft.digital_bank.web;

import com.medsoft.digital_bank.Dtos.CustomerDTO;
import com.medsoft.digital_bank.entities.Customer;
import com.medsoft.digital_bank.services.BankAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class CustomerRestController {

    private BankAccountService bankAccountService ;
    @GetMapping("/customers")
    public List <CustomerDTO> customers() {

        return bankAccountService.listCustomers() ;
        }

    @GetMapping("/customers/{id}")
    public CustomerDTO getCustomer(@PathVariable (name = "id") Long customerId) {
        return bankAccountService.getCustomer(customerId) ;
    }


    @PostMapping("/customers/save")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        return bankAccountService.saveCustomer(customerDTO) ;
    }


    @PutMapping("/customers/update/{customerId}")
    public CustomerDTO updateCustomer(@PathVariable Long customerId , @RequestBody CustomerDTO customerDTO   ) {
        customerDTO.setId(customerId);
        return bankAccountService.updateCustomer(customerDTO) ;
    }
    @DeleteMapping("/customers/{customerId}")
    public void deleteCustomer(@PathVariable Long customerId) {
         bankAccountService.delete(customerId);
    }


}
