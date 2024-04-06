package com.srikanth.BankApp.Service;


import com.srikanth.BankApp.Model.Customer;

import java.util.List;

public interface CustomerService {

    void addCustomer(Customer customer);

    Customer withdraw(int amount, Customer customer);

    boolean verifyCustomer(Customer customer, int pin);

    Customer deposit(Customer customer, int amount);

    List<Customer> listAllCustomers();

    void addBeneficiary(int acNo1, int acNo2);

    List<Customer> showBeneficiaryList(Customer customer);

}
