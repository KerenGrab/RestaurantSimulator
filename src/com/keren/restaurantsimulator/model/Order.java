package com.keren.restaurantsimulator.model;
import com.keren.restaurantsimulator.enums.OrderStatus;

import java.util.*;

/**
 * Order represents a single order for a table.
 *
 * It contains multiple OrderItem entries (each one is a Dish + quantity).
 * The order manages adding/removing dishes, counting total items,
 * and calculating the total price.
 */

public class Order {

    // Key = dish id, Value = OrderItem for that dish
    private final Map<Integer, OrderItem> itemsByDishId;
    private OrderStatus status = OrderStatus.CREATED;

    public Order() {
        // keeps insertion order (nice for printing)
        this.itemsByDishId = new LinkedHashMap<>();
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("status cannot be null");
        }
        this.status = status;
    }

    public void addDish(Dish dish, int quantity) {
        if (dish == null) {
            throw new IllegalArgumentException("dish cannot be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }

        int dishId = dish.getId();
        OrderItem existing = itemsByDishId.get(dishId);

        if (existing == null) {
            itemsByDishId.put(dishId, new OrderItem(dish, quantity));
        } else {
            existing.increaseQuantity(quantity);
        }
    }

    // Removes a dish line from the order completely, by dish id.
    public boolean removeDish(int dishId) {
        return itemsByDishId.remove(dishId) != null;
    }


    // Returns all order items (read-only).
    public Collection<OrderItem> getOrderItems() {
        return Collections.unmodifiableCollection(itemsByDishId.values());
    }

    public int getNumOfDishes() {
        int total = 0;
        for (OrderItem item : itemsByDishId.values()) {
            total += item.getQuantity();
        }
        return total;
    }

    public double getTotalPrice() {
        double total = 0.0;
        for (OrderItem item : itemsByDishId.values()) {
            total += item.getTotalPrice();
        }
        return total;

    }

    public int getNumOfLines() {
        return itemsByDishId.size(); // number of different dishes (line items)
    }

    public boolean isEmpty() {
        return itemsByDishId.isEmpty();
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Order:\n");
        for (OrderItem item : itemsByDishId.values()) {
            sb.append("  - ").append(item).append('\n');
        }
        sb.append("Total dishes: ").append(getNumOfDishes()).append('\n');
        sb.append("Total price: ").append(getTotalPrice()).append('\n');
        return sb.toString();
    }

}
