package com.srikanth.BankApp.Service.Impl;

import com.srikanth.BankApp.Repository.CustomerRepository;
import com.srikanth.BankApp.Model.Customer;
import com.srikanth.BankApp.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;


@Service
public class CustomerServiceImplementation implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImplementation(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    public void addCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    public Customer getCustomerByAcNo(int acNo) {
        return customerRepository.findById(acNo).orElse(null);
    }

    public Customer withdraw(int amount, Customer customer) {
        customer.setBalance(customer.getBalance() - amount);
        return customerRepository.save(customer);
    }

    public boolean verifyCustomer(Customer customer, int pin) {
        if (!ObjectUtils.isEmpty(customer)) {
            return pin == customer.getPin();
        }
        return false;
    }

    public Customer deposit(Customer customer, int amount) {
        if (!ObjectUtils.isEmpty(customer)) {
            customer.setBalance(customer.getBalance() + amount);
            return customerRepository.save(customer);
        }
        return customer;
    }

    public List<Customer> listAllCustomers() {
        return customerRepository.findAll();
    }

    public void addBeneficiary(int acNo1, int acNo2) {
        Customer customer1 = customerRepository.findById(acNo1).orElse(null);
        Customer customer2 = customerRepository.findById(acNo2).orElse(null);
        if (!ObjectUtils.isEmpty(customer1)) {
            if (!ObjectUtils.isEmpty(customer2)) {
                customer1.getBeneficiary().add(customer2);
                customerRepository.save(customer1);
            }
        }
    }

    public List<Customer> showBeneficiaryList(Customer customer) {
        if (!ObjectUtils.isEmpty(customer)) {
            return customer.getBeneficiary();
        }
        return null;
    }

}
