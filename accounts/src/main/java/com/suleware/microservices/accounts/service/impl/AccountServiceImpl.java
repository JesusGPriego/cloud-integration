package com.suleware.microservices.accounts.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.springframework.stereotype.Service;

import com.suleware.microservices.accounts.constants.AccountsConstants;
import com.suleware.microservices.accounts.dto.AccountsDto;
import com.suleware.microservices.accounts.dto.CustomerDto;
import com.suleware.microservices.accounts.entity.Accounts;
import com.suleware.microservices.accounts.entity.Customer;
import com.suleware.microservices.accounts.exception.CustomerAlreadyExistsException;
import com.suleware.microservices.accounts.exception.ResourceNotFoundException;
import com.suleware.microservices.accounts.mapper.AccountsMapper;
import com.suleware.microservices.accounts.mapper.CustomerMapper;
import com.suleware.microservices.accounts.repository.AccountsRepository;
import com.suleware.microservices.accounts.repository.CustomerRepository;
import com.suleware.microservices.accounts.service.IAccountService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements IAccountService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;

    @Override
    public void createAccount(CustomerDto customerDto) {
        Optional<Customer> oCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if (oCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException("Client already exists: " + customerDto.getMobileNumber());
        }
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Customer savedCustomer = customerRepository.save(customer);

        accountsRepository.save(createNewAccount(savedCustomer));

    }

    /**
     * @param customer - Customer Object
     * @return the new account details
     */
    private Accounts createNewAccount(Customer customer) {
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);

        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);

        return newAccount;
    }

    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        Customer c = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));
        Accounts a = accountsRepository.findByCustomerId(c.getCustomerId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Account", "customerID", c.getCustomerId().toString()));

        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(c, new CustomerDto());
        customerDto.setAccountsDto(AccountsMapper.mapToAccountDto(a, new AccountsDto()));
        return customerDto;
    }

    /**
     * @param customerDto - CustomerDto Object
     * @return boolean indicating if the update of Account details is successful or
     *         not
     */
    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated = false;
        AccountsDto accountsDto = customerDto.getAccountsDto();
        if (accountsDto != null) {
            Accounts accounts = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "AccountNumber",
                            accountsDto.getAccountNumber().toString()));
            AccountsMapper.mapToAccounts(accountsDto, accounts);
            accounts = accountsRepository.save(accounts);

            Long customerId = accounts.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "CustomerID", customerId.toString()));
            CustomerMapper.mapToCustomer(customerDto, customer);
            customerRepository.save(customer);
            isUpdated = true;
        }
        return isUpdated;
    }

    @Override
    public boolean deleteAccount(String mobileNumber) {

        Customer c = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));

        accountsRepository.deleteByCustomerId(c.getCustomerId());
        customerRepository.deleteById(c.getCustomerId());

        return true;
    }

    @Override
    public List<CustomerDto> findAll() {
        List<Customer> customers = customerRepository.findAll();
        List<Accounts> accounts = accountsRepository.findAll();
        List<CustomerDto> customersDto = customers.stream()
                .map(customer -> {

                    Accounts b = accounts.stream()
                            .filter(account -> account.getCustomerId().equals(customer.getCustomerId())).findAny()
                            .get();

                    CustomerDto cDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
                    cDto.setAccountsDto(AccountsMapper.mapToAccountDto(b, new AccountsDto()));
                    return cDto;
                }).toList();
        return customersDto;
    }

}
