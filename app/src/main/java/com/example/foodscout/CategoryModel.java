package com.example.foodscout;

public class CategoryModel {
    String Image;
    String Name;

    public CategoryModel() {
    }

    public CategoryModel(String image,String name) {
        Image = image;
        Name = name;
    }



    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
