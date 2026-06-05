package com.company.clothingerp.controller.web;

import com.company.clothingerp.model.Customer;
import com.company.clothingerp.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/crm")
public class CrmController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public String showCrm(Model model) {
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("newCustomer", new Customer());
        return "crm";
    }

    @PostMapping("/customer")
    public String addCustomer(@ModelAttribute Customer customer) {
        customer.setStatus("ACTIVE");
        customerService.saveCustomer(customer);
        return "redirect:/crm";
    }
}
