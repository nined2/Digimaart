package com.example.digimart;
public class Product {
    private int id;
    private String name;
    private double price;
    private String imageUrl;

    public Product(int id, String name, double price,String imageurl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageurl;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public  String getImageUrl(){ return imageUrl;}
    @Override
    public String toString() {
        return name + " - $" + price;
    }
}
