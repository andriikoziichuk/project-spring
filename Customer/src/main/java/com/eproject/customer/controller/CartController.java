package com.eproject.customer.controller;

import com.eproject.library.model.Customer;
import com.eproject.library.model.Product;
import com.eproject.library.model.ShoppingCart;
import com.eproject.library.service.CustomerService;
import com.eproject.library.service.ProductService;
import com.eproject.library.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
public class CartController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ShoppingCartService cartService;

    @Autowired
    private ProductService productService;

    @GetMapping("/cart")
    public String cart(Model model, Principal principal, HttpSession session){
        if(principal == null){
            return "redirect:/login";
        }
        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);
        ShoppingCart shoppingCart = customer.getShoppingCart();
        model.addAttribute("pageName", "Кошик");
        if(shoppingCart == null){
            model.addAttribute("check", "No item in your cart");
        } else {
            session.setAttribute("totalItems", shoppingCart.getTotalItems());
            model.addAttribute("subTotal", shoppingCart.getTotalPrice());
            model.addAttribute("shoppingCart", shoppingCart);
        }
        return "cart";
    }

    @PostMapping("/add-to-cart")
    public String addItemToCart(
            @RequestParam("id") Long productId,
            @RequestParam(value = "quantity", required = false, defaultValue = "1") int quantity,
            Principal principal,
            HttpServletRequest request){

        if(principal == null){
            return "redirect:/login";
        }
        Product product = productService.getProductById(productId);
        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);

        ShoppingCart cart = cartService.addItemToCart(product, quantity, customer);
        return "redirect:" + request.getHeader("Referer");
    }

    @RequestMapping(value = "/update-cart", method = RequestMethod.POST, params = "action=update")
    public String updateCart(@RequestParam("quantity") int quantity,
                             @RequestParam("id") Long id,
                             Model model,
                             Principal principal) {
        if (principal == null)
            return "redirect:/login";
        else {
            String username = principal.getName();
            Customer customer = customerService.findByUsername(username);
            Product product = productService.getProductById(id);
            ShoppingCart shoppingCart = cartService.updateItemInCart(product, quantity, customer);

            model.addAttribute("shoppingCart",shoppingCart);
            return "redirect:/cart";
        }
    }

    @RequestMapping(value = "/update-cart", method = RequestMethod.POST, params = "action=delete")
    public String deleteItemFromCart(@RequestParam("id") Long id,
                             Model model,
                             Principal principal) {
        if (principal == null)
            return "redirect:/login";
        else {
            String username = principal.getName();
            Customer customer = customerService.findByUsername(username);
            Product product = productService.getProductById(id);
            ShoppingCart shoppingCart = cartService.deleteItemFromCart(product, customer);

            model.addAttribute("shoppingCart",shoppingCart);
            return "redirect:/cart";
        }
    }
}
