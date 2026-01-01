package com.keren.restaurantsimulator.model;
import com.keren.restaurantsimulator.enums.OrderStatus;
import com.keren.restaurantsimulator.enums.TableStatus;
import com.keren.restaurantsimulator.model.Order;


public class Table {
    private final int tableNumber;
    private final int capacity;
    private TableStatus status;
    private Order currentOrder;


    public Table(int tableNumber, int seats) {
        if (seats <= 0) {
            throw new IllegalArgumentException("seats must be positive");
        }
        this.tableNumber = tableNumber;
        this.capacity = seats;
        this.status = TableStatus.FREE;
        this.currentOrder = null;
    }
    public Order getOrder() {
        return currentOrder;
    }

    public void setOrder(Order order) {
        this.currentOrder = order;
    }

    public void clearOrder() {
        this.currentOrder = null;
    }

    public int getCapacity() {
        return capacity;
    }

    public TableStatus getStatus() {
        return status;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public boolean hasOrder() {
        return currentOrder != null;
    }


    /**
     * Clears the table (after payment).
     * Resets status to FREE and removes the current order.
     */

    public void clear() {
        this.status = TableStatus.FREE;
        this.currentOrder = null;}


    /**
     * Seats customers at the table.
     * This marks the table as OCCUPIED.
     * (Capacity check is optional; you can add it later if you track customer count.)
     */
    public void seat(int numOfCustomers) {
        if (numOfCustomers <= 0) {
            throw new IllegalArgumentException("numOfCustomers must be positive");
        }
        if (numOfCustomers > capacity) {
            throw new IllegalArgumentException("Too many customers for table capacity");
        }
        this.status = TableStatus.OCCUPIED;

    }

    public void openNewOrder() {
        if (status != TableStatus.OCCUPIED) {
            throw new IllegalStateException("Table must be OCCUPIED to open a new order");
        }
        if (currentOrder != null) {
            throw new IllegalStateException("Table already has an open order");
        }
        this.currentOrder = new Order();
    }

    public void requestBill() {
        if (status != TableStatus.OCCUPIED) {
            throw new IllegalStateException("Table must be OCCUPIED to request bill");
        }
        if (currentOrder == null || currentOrder.isEmpty()) {
            throw new IllegalStateException("Cannot request bill without an order");
        }
        this.status = TableStatus.WAITING_FOR_BILL;
    }

    //Current bill amount for this table.
    public double getBill() {
        return (currentOrder == null) ? 0.0 : currentOrder.getTotalPrice();
    }

    public void setStatus(TableStatus status){
        if (status == null) {
            throw new IllegalArgumentException("status cannot be null");
        }
        this.status = status;
    }
}



