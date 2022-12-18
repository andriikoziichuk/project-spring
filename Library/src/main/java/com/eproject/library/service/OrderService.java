package com.eproject.library.service;

import com.eproject.library.model.ShoppingCart;

public interface OrderService {
    void saveOrder(ShoppingCart shoppingCart);
}
