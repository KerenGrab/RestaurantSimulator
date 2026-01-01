package com.keren.restaurantsimulator.model;

public class Dish {
    // I will create a dish id, so I can numerate them later in the menu
    private final int id;
    private final String name;
    private final double price;
    private final int prepTimeMinutes;

    public Dish(int id, String name, double price, int prepTimeMinutes){
        this.id = id;
        this.name = name;
        this.price = price;
        this.prepTimeMinutes = prepTimeMinutes;
    }
    public int getId(){return id;}
    public String getName(){return name;}
    public double getPrice(){return price;}
    public int getPrepTimeMinutes(){return prepTimeMinutes;}

    @Override
    public String toString(){
        return  "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", prepTimeMinutes=" + prepTimeMinutes +
                '}';
    }
}
