package com.keren.restaurantsimulator.service;

import com.keren.restaurantsimulator.enums.TableStatus;
import com.keren.restaurantsimulator.model.Restaurant;
import com.keren.restaurantsimulator.model.Table;

public class TableService {

    private final Restaurant restaurant;

    public TableService(Restaurant restaurant) {
        if (restaurant == null) {
            throw new IllegalArgumentException("restaurant cannot be null");
        }
        this.restaurant = restaurant;
    }

    public void addTable(Table table) {
        restaurant.addTable(table);
    }

    public boolean isTaken(int tableNumber) {
        return restaurant.getTableById(tableNumber).getStatus() == TableStatus.OCCUPIED;
    }

    public TableStatus getStatus(int tableNumber) {
        return restaurant.getTableById(tableNumber).getStatus();
    }

    public int getNumOfTables() {
        return restaurant.size();
    }
}
