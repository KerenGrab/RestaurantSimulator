package com.keren.restaurantsimulator.service;

import com.keren.restaurantsimulator.enums.OrderStatus;
import com.keren.restaurantsimulator.enums.TableStatus;
import com.keren.restaurantsimulator.model.Order;
import com.keren.restaurantsimulator.model.Restaurant;
import com.keren.restaurantsimulator.model.Table;

public class BillingService {

    private final Restaurant restaurant;

    public BillingService(Restaurant restaurant) {
        if (restaurant == null) {
            throw new IllegalArgumentException("restaurant cannot be null");
        }
        this.restaurant = restaurant;
    }

    private Table getExistingTable(int tableId) {
        return restaurant.getTableById(tableId);
    }

    public void markServed(int tableId) {
        Table table = getExistingTable(tableId);

        Order order = table.getOrder();
        if (order == null) {
            throw new IllegalStateException("Table " + tableId + " has no active order");
        }

        order.setStatus(OrderStatus.SERVED);
        // חשוב: לא מעבירים אוטומטית ל-WAITING_FOR_BILL.
        // זה קורה רק כשקוראים requestBill().
    }

    public void requestBill(int tableId) {
        Table table = getExistingTable(tableId);
        table.requestBill(); // משתמשים בכלל העסקי שכבר הגדרת ב-Table
    }

    public double calculateBill(int tableId) {
        Table table = getExistingTable(tableId);
        return table.getBill(); // לא לשכפל לוגיקה
    }

    public double pay(int tableId) {
        Table table = getExistingTable(tableId);

        if (table.getStatus() != TableStatus.WAITING_FOR_BILL) {
            throw new IllegalStateException("Table " + tableId + " is not waiting for bill");
        }

        Order order = table.getOrder();
        if (order == null) {
            throw new IllegalStateException("Table " + tableId + " has no active order");
        }

        double total = order.getTotalPrice();
        order.setStatus(OrderStatus.PAID);

        // clear() מאפס גם סטטוס וגם הזמנה
        table.clear();

        return total;
    }
}
