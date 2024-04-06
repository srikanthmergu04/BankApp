package com.srikanth.BankApp.controller;

import com.srikanth.BankApp.Model.Customer;
import com.srikanth.BankApp.Service.Impl.CustomerServiceImplementation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Controller
public class CustomerController {

    @Autowired
    CustomerServiceImplementation service;

    @RequestMapping("/CreateAccount")
    public ModelAndView registerStudents(ModelAndView modelAndView) {
        modelAndView.addObject("customer", new Customer());
        modelAndView.setViewName("RegisterCustomer");
        return modelAndView;
    }

    @RequestMapping(value = "/registerSuccess", method = RequestMethod.POST)
    public ModelAndView registerSuccess(@ModelAttribute("customer") Customer customer, ModelAndView modelAndView) {
        service.addCustomer(customer);
        modelAndView.addObject("customer", customer);
        modelAndView.setViewName("DisplayAcDetails");
        return modelAndView;
    }


    @RequestMapping("/BalanceEnquiry")
    public ModelAndView balanceEnquiry(HttpServletRequest req, ModelAndView modelAndView) {
        int id = Integer.parseInt(req.getParameter("acno"));
        Customer customer = service.getCustomerByAcNo(id);
        if (ObjectUtils.isEmpty(customer)) {
            modelAndView.addObject("errorMessage", "Invalid Customer");
            modelAndView.setViewName("errorMessage");
            return modelAndView;
        }
        modelAndView.addObject("balance", customer.getBalance());
        modelAndView.setViewName("ShowBalance");
        return modelAndView;
    }

    @RequestMapping("/withdraw")
    public ModelAndView withdraw(HttpServletRequest req, ModelAndView modelAndView) {
        int acNo = Integer.parseInt(req.getParameter("acNo"));
        Customer customer = service.getCustomerByAcNo(acNo);
        if (ObjectUtils.isEmpty(customer)) {
            modelAndView.addObject("errorMessage", "Invalid Customer");
            modelAndView.setViewName("errorMessage");
            return modelAndView;
        }

        int pin = Integer.parseInt(req.getParameter("pin"));
        int amount = Integer.parseInt(req.getParameter("amount"));

        if (service.verifyCustomer(customer, pin)) {

            if (customer.getBalance() < amount) {
                modelAndView.addObject("errorMessage", "Insufficient Funds");
                modelAndView.setViewName("errorMessage");
                return modelAndView;
            }

            Customer updatedCustomer = service.withdraw(amount, customer);
            modelAndView.addObject("balance", updatedCustomer.getBalance());
            modelAndView.setViewName("DisplayBal");
            return modelAndView;
        } else {
            modelAndView.addObject("errorMessage", "Incorrect PIN");
            modelAndView.setViewName("errorMessage");
            return modelAndView;
        }
    }

    @RequestMapping("/deposit")
    public ModelAndView deposit(HttpServletRequest req, ModelAndView modelAndView) {
        int acNo = Integer.parseInt(req.getParameter("acNo"));
        int amount = Integer.parseInt(req.getParameter("amount"));

        Customer customer = service.getCustomerByAcNo(acNo);
        if (ObjectUtils.isEmpty(customer)) {
            modelAndView.addObject("errorMessage", "Invalid Customer");
            modelAndView.setViewName("errorMessage");
            return modelAndView;
        }

        Customer updatedCustomer = service.deposit(customer, amount);
        modelAndView.addObject("balance", updatedCustomer.getBalance());
        modelAndView.setViewName("depositSuccess");
        return modelAndView;
    }

    @RequestMapping("/fundTransfer")
    public ModelAndView fundTransfer(HttpServletRequest req, ModelAndView modelAndView) {
        int acNo1 = Integer.parseInt(req.getParameter("acNo1"));
        int pin = Integer.parseInt(req.getParameter("pin"));

        int amount = Integer.parseInt(req.getParameter("amount"));
        int acNo2 = Integer.parseInt(req.getParameter("acNo2"));

        Customer customer1 = service.getCustomerByAcNo(acNo1);
        if (ObjectUtils.isEmpty(customer1)) {
            modelAndView.addObject("errorMessage", "Invalid Customer");
            modelAndView.setViewName("errorMessage");
            return modelAndView;
        }

        if (customer1.getBalance() < amount) {
            modelAndView.addObject("errorMessage", "Insufficient Funds");
            modelAndView.setViewName("errorMessage");
        }

        Customer customer2 = service.getCustomerByAcNo(acNo2);
        if (ObjectUtils.isEmpty(customer2)) {
            modelAndView.addObject("errorMessage", "Invalid Beneficiary");
            modelAndView.setViewName("errorMessage");
            return modelAndView;
        }

        if (service.verifyCustomer(customer1, pin)) {
            Customer updatedCustomer = service.withdraw(amount, customer1);
            service.deposit(customer2, amount);
            modelAndView.addObject("balance", updatedCustomer.getBalance());
            modelAndView.setViewName("DisplayBal");
            return modelAndView;
        } else {
            modelAndView.addObject("errorMessage", "Incorrect PIN");
            modelAndView.setViewName("errorMessage");
            return modelAndView;
        }
    }

    @RequestMapping("/customerList")
    public ModelAndView getCustomerList(ModelAndView modelAndView) {
        modelAndView.addObject("list", service.listAllCustomers());
        modelAndView.setViewName("CustomerDetails");
        return modelAndView;
    }

    @RequestMapping("/addBeneficiary")
    public ModelAndView addBeneficiary(HttpServletRequest req, ModelAndView modelAndView) {
        int acNo1 = Integer.parseInt(req.getParameter("acNo1"));
        int acNo2 = Integer.parseInt(req.getParameter("acNo2"));

        Customer customer1 = service.getCustomerByAcNo(acNo1);
        if (ObjectUtils.isEmpty(customer1)) {
            modelAndView.addObject("errorMessage", "Invalid Customer");
            modelAndView.setViewName("errorMessage");
            return modelAndView;
        }

        Customer customer2 = service.getCustomerByAcNo(acNo2);
        if (ObjectUtils.isEmpty(customer2)) {
            modelAndView.addObject("errorMessage", "Invalid Beneficiary");
            modelAndView.setViewName("errorMessage");
            return modelAndView;
        }

        service.addBeneficiary(acNo1, acNo2);
        modelAndView.setViewName("beneficiarySuccess");
        return modelAndView;
    }

    @RequestMapping("/showBeneficiaryList")
    public ModelAndView showBeneficiaryList(HttpServletRequest req, ModelAndView modelAndView) {
        int acNo = Integer.parseInt(req.getParameter("acNo"));

        Customer customer = service.getCustomerByAcNo(acNo);
        if (ObjectUtils.isEmpty(customer)) {
            modelAndView.addObject("errorMessage", "Invalid Customer");
            modelAndView.setViewName("errorMessage");
            return modelAndView;
        }

        List<Customer> list = service.showBeneficiaryList(customer);

        modelAndView.addObject("acNum", acNo);
        modelAndView.addObject("BeneficiaryList", list);
        modelAndView.setViewName("displayBeneficiaryList");
        return modelAndView;
    }

}
