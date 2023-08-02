package com.example.foodscout;

public class HomeExclusiveModel {
    String name;
    String img;
    Double item_price;
    String description;

    public HomeExclusiveModel() {
    }

    public HomeExclusiveModel(String name, String img,Double price,String description) {
        this.name = name;
        this.img = img;
        this.item_price=price;
        this.description=description;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getItem_price() {
        return item_price;
    }

    public void setItem_price(Double item_price) {
        this.item_price = item_price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
