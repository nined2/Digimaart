package com.example.digimart;

public class Product1 {
    private String name;
    private double price;
    private int quantity;

    public Product1(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}

