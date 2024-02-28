package com.suleware.microservices.accounts.service;

import java.util.List;

import com.suleware.microservices.accounts.dto.CustomerDto;

public interface IAccountService {

    List<CustomerDto> findAll();

    /**
     * 
     * @param customerDto - CustomerDto Object
     */
    void createAccount(CustomerDto customerDto);

    /**
     * 
     * @param mobileNumber - customer mobile number
     * @return - CustomerDto Object
     */
    CustomerDto fetchAccount(String mobileNumber);

    /**
     * 
     * @param customerDto - customer which account will be updated
     * @return true if update success
     */
    boolean updateAccount(CustomerDto customerDto);

    /**
     * 
     * @param customerDto - customer which account will be updated
     * @return true if delete success
     */
    boolean deleteAccount(String mobileNumber);

}
