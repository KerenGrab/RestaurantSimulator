package com.keren.restaurantsimulator.service;

import com.keren.restaurantsimulator.enums.OrderStatus;
import com.keren.restaurantsimulator.model.Order;

import java.util.LinkedHashMap;
import java.util.Map;

public class OrderService {

    private int nextOrderId = 1;
    private final Map<Integer, Order> ordersById = new LinkedHashMap<>();

    public int addOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("order cannot be null");
        }

        // מוודאים מצב התחלה עקבי
        order.setStatus(OrderStatus.CREATED);

        int id = nextOrderId++;
        ordersById.put(id, order);
        return id;
    }

    public int getNumOfOrders() {
        return ordersById.size();
    }

    public Order getOrder(int orderId) {
        Order order = ordersById.get(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order " + orderId + " not found");
        }
        return order;
    }

    public void removeOrder(int orderId) {
        Order removed = ordersById.remove(orderId);
        if (removed == null) {
            throw new IllegalArgumentException("Order " + orderId + " not found");
        }
    }

    public Map<Integer, Order> getAllOrders() {
        return Map.copyOf(ordersById);
    }
}
