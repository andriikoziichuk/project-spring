package com.eproject.customer.controller;

import com.eproject.library.dto.ProductDTO;
import com.eproject.library.model.Category;
import com.eproject.library.model.Customer;
import com.eproject.library.model.ShoppingCart;
import com.eproject.library.service.CategoryService;
import com.eproject.library.service.CustomerService;
import com.eproject.library.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CustomerService customerService;

    @GetMapping("/about-us")
    public String home(Model model,
                       Principal principal,
                       HttpSession httpSession) {
        model.addAttribute("pageName", "Про нас");
        if (principal != null) {
            httpSession.setAttribute("username", principal.getName());
            Customer customer = customerService.findByUsername(principal.getName());
            ShoppingCart shoppingCart = customer.getShoppingCart();
            httpSession.setAttribute("totalItems", shoppingCart.getTotalItems());
        } else
            httpSession.removeAttribute("username");
        return "home";
    }

    @GetMapping(value = {"/index", "/"})
    public String index(Model model){
        List<Category> categories = categoryService.findAllByActivated();
        List<ProductDTO> productDTOS = productService.findAll();
        model.addAttribute("pageName", "Головна");
        model.addAttribute("categories", categories);
        model.addAttribute("products", productDTOS);
        return "index";
    }
}
