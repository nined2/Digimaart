package com.example.digimart;
public class Product {
    private int id;
    private String name;
    private double price;
    private String imageurl;
    private int quantity;

    public Product(int id, String name, double price,String imageurl,int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageurl = imageurl;
        this.quantity = quantity;
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

    public  String getImageUrl(){ return imageurl;}

    public  int getQuantity(){ return quantity;}


    @Override
    public String toString() {
        return name + " - $" + price;
    }
}
