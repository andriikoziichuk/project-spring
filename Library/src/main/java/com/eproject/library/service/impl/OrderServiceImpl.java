package com.eproject.library.service.impl;

import com.eproject.library.model.CartItem;
import com.eproject.library.model.Order;
import com.eproject.library.model.OrderDetail;
import com.eproject.library.model.ShoppingCart;
import com.eproject.library.repository.CartItemRepository;
import com.eproject.library.repository.OrderDetailRepository;
import com.eproject.library.repository.OrderRepository;
import com.eproject.library.repository.ShoppingCartRepository;
import com.eproject.library.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public void saveOrder(ShoppingCart shoppingCart) {
        Order order = new Order();
        order.setOrderStatus("PENDING");

        order.setOrderDate(new Date());

        order.setDeliveryDate(OrderServiceImpl.addMonths(order.getOrderDate(), 1));
        order.setCustomer(shoppingCart.getCustomer());
        order.setTotalPrice(shoppingCart.getTotalPrice());

        List<OrderDetail> orderDetailList = new ArrayList<>();

        for (CartItem item : shoppingCart.getCartItem()) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setQuantity(item.getQuantity());
            detail.setProduct(item.getProduct());
            detail.setUnitPrice(item.getProduct().getCostPrice());
            orderDetailRepository.save(detail);
            orderDetailList.add(detail);
            cartItemRepository.delete(item);
        }
        order.setOrderDetailList(orderDetailList);

        shoppingCart.setCartItem(new HashSet<>());
        shoppingCart.setTotalItems(0);
        shoppingCart.setTotalPrice(0);
        shoppingCartRepository.save(shoppingCart);
        orderRepository.save(order);
    }

    public static Date addMonths(Date today, int monthsToAdd) {
        if (today != null) {
            java.util.Calendar c = java.util.Calendar.getInstance();
            c.setTime(today);
            c.add(Calendar.MONTH, monthsToAdd);
            return c.getTime();
        } else {
            return null;
        }
    }
}
