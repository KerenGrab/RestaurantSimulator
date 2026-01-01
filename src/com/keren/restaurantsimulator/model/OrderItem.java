package com.keren.restaurantsimulator.model;

/**
 * OrderItem represents a single line item within an order.
 *
 * Each OrderItem links a specific Dish to a quantity ordered by the customer.
 * It is responsible for storing order-specific information related to that dish,
 * such as the quantity and the total price calculation for this item.
 *
 * This class allows modifying the quantity of the dish within the order
 * and provides utility methods for price calculation and display.
 */

public class OrderItem {
    private final Dish dish;
    private int quantity;

    public OrderItem(Dish dish, int quantity){
        if (dish==null){
            throw new IllegalArgumentException("dish cannot be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
        this.dish = dish;
        this.quantity = quantity;
    }

    // Getters for the private fields
    public Dish getDish(){
            return dish;
        }
    public int getQuantity() {
        return quantity;
    }

    // Calculating the total price of all the items together
    public double getTotalPrice(){
            return dish.getPrice() * quantity;
    }

    // Increase quantity (e.g., add more of the same dish)
    public void increaseQuantity(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
        this.quantity += amount;
    }

    public void decreaseQuantity(int amount){
        if (amount <=0){
            throw new IllegalArgumentException("amount must be positive");
        }
        if (this.quantity - amount <= 0) {
            throw new IllegalArgumentException("quantity cannot be zero or negative");
        }
        this.quantity -= amount;
    }

    @Override
    public String toString(){
        return "OrderItem{" +
                "dish=" + dish.getName() +
                ", quantity=" + quantity +
                ", totalPrice=" + getTotalPrice() +
                '}';
    }

}