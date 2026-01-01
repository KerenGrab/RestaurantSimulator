package com.keren.restaurantsimulator.model;
import com.keren.restaurantsimulator.exception.DishNotFoundException;
import java.util.*;

/**
 * Menu holds the restaurant's dishes and allows lookup by dish id.
 * This is a simple in-memory menu (no files / DB yet).
 * Rules:
 * - Dish id must be unique in the menu.
 * - If you try to add a dish with an existing id -> error.
 **/

public class Menu {

    // Creating a dictionary like way to look up the dishes
    // the key is the dish id and the value is the dish object
    private final Map<Integer, Dish> dishesById;

    public Menu() {
        // This keeps the insertion order for easier work later on.
        this.dishesById = new LinkedHashMap<>();
    }

    public void addDish(Dish dish) {
        if (dish == null) {
            throw new IllegalArgumentException("dish cannot be null");
        }
        int id = dish.getId();
        if (dishesById.containsKey(id)) {
            throw new IllegalArgumentException("Dish with id " + id + " already exists in the menu");
        }
        dishesById.put(id, dish);
    }

    public boolean removeDish(int dishId) {
        return dishesById.remove(dishId) != null;
    }

    // Finds a dish by its id
    public Optional<Dish> findDishById(int dishId) {
        return Optional.ofNullable(dishesById.get(dishId));
    }

    public Dish getDishOrThrow(int dishId) {
        Dish dish = dishesById.get(dishId);
        if (dish == null) {
            throw new DishNotFoundException("Dish with id " + dishId + " not found");
        }
        return dish;
    }
    public Collection<Dish> listDishes() {
        return Collections.unmodifiableCollection(dishesById.values());
    }
    public int size() {
        return dishesById.size();
    }
    public boolean containsDish(int dishId) {
        return dishesById.containsKey(dishId);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Menu:\n");
        for (Dish dish : dishesById.values()) {
            sb.append("  - ").append(dish).append('\n');
        }
        return sb.toString();
    }
}