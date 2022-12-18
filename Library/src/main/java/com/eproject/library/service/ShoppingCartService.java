package com.eproject.library.service;

import com.eproject.library.model.Customer;
import com.eproject.library.model.Product;
import com.eproject.library.model.ShoppingCart;
import org.springframework.stereotype.Service;

public interface ShoppingCartService {
    ShoppingCart addItemToCart(Product product, int quantity, Customer customer);

    ShoppingCart updateItemInCart(Product product, int quantity, Customer customer);

    ShoppingCart deleteItemFromCart(Product product, Customer customer);
}
