package com.keren.restaurantsimulator.service;

import com.keren.restaurantsimulator.enums.OrderStatus;
import com.keren.restaurantsimulator.exception.InvalidOrderStateException;
import com.keren.restaurantsimulator.model.Order;

public class KitchenService {

    private final OrderService orderService;

    public KitchenService(OrderService orderService) {
        if (orderService == null) {
            throw new IllegalArgumentException("orderService cannot be null");
        }
        this.orderService = orderService;
    }

    private Order getExistingOrder(int orderId) {
        return orderService.getOrder(orderId);
    }

    private void requireStatus(Order order, OrderStatus... allowed) {
        OrderStatus current = order.getStatus();
        for (OrderStatus a : allowed) {
            if (current == a) return;
        }
        throw new InvalidOrderStateException(
                "Invalid order state: " + current + ". Allowed: " + java.util.Arrays.toString(allowed)
        );
    }

    public void submitOrderToKitchen(int orderId) {
        Order order = getExistingOrder(orderId);
        requireStatus(order, OrderStatus.CREATED);
        order.setStatus(OrderStatus.SUBMITTED);
    }

    public void startPreparing(int orderId) {
        Order order = getExistingOrder(orderId);
        requireStatus(order, OrderStatus.SUBMITTED);
        order.setStatus(OrderStatus.IN_PREP);
    }

    public void orderIsReady(int orderId) {
        Order order = getExistingOrder(orderId);
        requireStatus(order, OrderStatus.IN_PREP);
        order.setStatus(OrderStatus.READY);
    }

    public boolean removeDishFromOrder(int orderId, int dishId) {
        Order order = getExistingOrder(orderId);
        return order.removeDish(dishId);
    }
}
