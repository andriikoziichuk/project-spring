package com.eproject.admin.controller;

import com.eproject.library.alghorithms.BinarySearch;
import com.eproject.library.alghorithms.CustomerComparator;
import com.eproject.library.alghorithms.QuickSort;
import com.eproject.library.model.Customer;
import com.eproject.library.model.Role;
import com.eproject.library.repository.RoleRepository;
import com.eproject.library.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AdminController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/customers/{pageNo}")
    public String customers(Model model,
                            @PathVariable("pageNo") int pageNo,
                            Principal principal) {
        if (principal == null)
            return "redirect:/login";

        List<Customer> customers = customerService.findAll();
        model.addAttribute("customers", customers);
        model.addAttribute("size", customers.size());
        model.addAttribute("title", "Customers manager");

        Page<Customer> customerPage = customerService.pageProducts(0);
        model.addAttribute("totalPages", customerPage.getTotalPages());
        model.addAttribute("currentPage", 0);

        return "customers";
    }

    @GetMapping("/update-customer/{id}")
    public String updateProduct(@PathVariable("id")Long id,
                                Model model,
                                Principal principal){
        if (principal == null)
            return "redirect:/login";

        model.addAttribute("title", "Update customers");
        List<Role> roles = roleRepository.findAll();
        Customer customer = customerService.getById(id);
        model.addAttribute("roles", roles);
        model.addAttribute("customer", customer);

        return "update-customer";
    }

    @PostMapping("/update-customer/{id}")
    public String processUpdate(
            @ModelAttribute("customer") Customer customer,
            @RequestParam("imageCustomer") MultipartFile imageCustomer,
            RedirectAttributes redirectAttributes){
        try {
            customerService.update(imageCustomer, customer);
            redirectAttributes.addFlashAttribute("success", "Update successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to update!");
        }
        return "redirect:/customers/0";
    }

    @GetMapping("/search")
    public String searchCustomer(@RequestParam("keyword") String username,
                                 Model model,
                                 Principal principal) {

        if (principal == null)
            return "redirect:/login";

        List<Customer> customers = QuickSort.qSort(customerService.findAll(), new CustomerComparator());
        int index = BinarySearch.binarySearch(customers.stream().map(Customer::getUsername).collect(Collectors.toList()), username);
        if (index != -1)
            return "redirect:/update-customer/" + customers.get(index).getId();

        return "redirect:/customers/0";
    }
}
