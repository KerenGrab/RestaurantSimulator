package com.keren.restaurantsimulator.app;

import com.keren.restaurantsimulator.model.Dish;
import com.keren.restaurantsimulator.model.Menu;
import com.keren.restaurantsimulator.model.Restaurant;
import com.keren.restaurantsimulator.model.Table;

public final class DemoData {

    private DemoData() {}

    public static Menu createDemoMenu() {
        Menu menu = new Menu();

        menu.addDish(new Dish(1, "Margherita Pizza", 48.0, 15));
        menu.addDish(new Dish(2, "Pasta Alfredo", 54.0, 18));
        menu.addDish(new Dish(3, "Greek Salad", 34.0, 7));
        menu.addDish(new Dish(4, "Cheeseburger", 59.0, 14));
        menu.addDish(new Dish(5, "French Fries", 18.0, 6));
        menu.addDish(new Dish(6, "Cola", 12.0, 1));
        menu.addDish(new Dish(7, "Ice Cream", 22.0, 3));

        return menu;
    }

    public static Restaurant createDemoRestaurant() {
        Menu menu = createDemoMenu();
        Restaurant restaurant = new Restaurant(menu);

        // Add a few tables
        restaurant.addTable(new Table(1, 2));
        restaurant.addTable(new Table(2, 4));
        restaurant.addTable(new Table(3, 4));
        restaurant.addTable(new Table(4, 6));
        restaurant.addTable(new Table(5, 8));

        return restaurant;
    }
}
