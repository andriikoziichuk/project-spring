package com.eproject.customer.controller;

import com.eproject.library.dto.CustomerDTO;
import com.eproject.library.model.Customer;
import com.eproject.library.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class AuthController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("customerDTO", new CustomerDTO());
        return "register";
    }

    @PostMapping("/do-register")
    public String processRegister(@Valid @ModelAttribute("customerDTO") CustomerDTO customerDTO,
                                  BindingResult bindingResult,
                                  Model model,
                                  RedirectAttributes attributes) {
        try {
            if (bindingResult.hasErrors()) {
                model.addAttribute("customerDTO", customerDTO);
                return "register";
            }
            Customer customer = customerService.findByUsername(customerDTO.getUsername());
            if (customer != null) {
                model.addAttribute("username", "Username have been registered!");
                model.addAttribute("customerDTO", customerDTO);
                return "register";
            }
            if (customerDTO.getPassword().equals(customerDTO.getRepeatPassword())) {
                customerDTO.setPassword(passwordEncoder.encode(customerDTO.getPassword()));
                CustomerDTO save = customerService.save(customerDTO);
                model.addAttribute("success", "Register successfully");
                return "register";
            } else {
                model.addAttribute("password", "Password is not same");
                model.addAttribute("customerDTO", customerDTO);
                return "register";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Server have ran some problems");
            model.addAttribute("customerDTO", customerDTO);
        }

        return "register";

    }
}
