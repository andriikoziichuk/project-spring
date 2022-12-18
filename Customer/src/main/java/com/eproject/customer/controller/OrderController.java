package com.eproject.customer.controller;

import com.eproject.library.model.Customer;
import com.eproject.library.model.Order;
import com.eproject.library.model.ShoppingCart;
import com.eproject.library.service.CustomerService;
import com.eproject.library.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/check-out")
    public String checkout(Model model, Principal principal) {

        if (principal == null)
            return "redirect:/login";
        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);
        model.addAttribute("pageName", "Оформлення замовлення");
        if (customer.getPhoneNumber().trim().isEmpty() || customer.getAddress().trim().isEmpty()
                || customer.getCity().trim().isEmpty() || customer.getCountry().trim().isEmpty()) {
            model.addAttribute("customer", customer);
            model.addAttribute("error", "You must fill the information after checkout!");
            return "account";
        } else {
            model.addAttribute("customer", customer);
            ShoppingCart cart = customer.getShoppingCart();
            model.addAttribute("cart", cart);
        }

        return "checkout";
    }

    @GetMapping("/order")
    public String order(Model model, Principal principal) {

        if (principal == null)
            return "redirect:/login";

        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);
        List<Order> orderList = customer.getOrders();
        model.addAttribute("orders", orderList);
        model.addAttribute("pageName", "Замовлення");
        return "order";
    }

    @RequestMapping(value = "/save-order", method = {RequestMethod.GET, RequestMethod.PUT})
    public String saveOrder(Model model, Principal principal) {

        if (principal == null)
            return "redirect:/login";

        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);
        ShoppingCart cart = customer.getShoppingCart();
        orderService.saveOrder(cart);
        model.addAttribute("pageName", "Замовлення");
        return "redirect:/order";

    }
}
