package com.example.tiendaonline;

public class Producto {
    private int id;        // Agrega el campo id
    private String name;
    private double price;

    // Constructor
    public Producto(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}


