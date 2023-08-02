package com.example.foodscout;

public class AddToCartModel {
    String image;
    String name;
    Double price;
    Double qty;

    public AddToCartModel() {
    }

    public AddToCartModel(String image, String name, Double price,Double qty) {
        this.image = image;
        this.name = name;
        this.price = price;
        this.qty=qty;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }
}
