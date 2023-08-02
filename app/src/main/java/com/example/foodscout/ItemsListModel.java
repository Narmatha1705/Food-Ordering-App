package com.example.foodscout;

import java.util.Comparator;

public class ItemsListModel implements Comparable<ItemsListModel>{
    String item_name;
    String item_img;
    Double item_price;
    String description;

    public ItemsListModel() {
    }

    public ItemsListModel(String item_name, String item_img, Double item_price,String description) {
        this.item_name = item_name;
        this.item_img = item_img;
        this.item_price = item_price;
        this.description=description;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_img() {
        return item_img;
    }

    public void setItem_img(String item_img) {
        this.item_img = item_img;
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

    @Override
    public int compareTo(ItemsListModel o) {
        Double pr=o.getItem_price();
        return this.item_name.compareTo(getItem_name());
        //return (this.item_price) - Integer.valueOf(String.valueOf(pr));
    }

    public static Comparator<ItemsListModel> nameSort = new Comparator<ItemsListModel>() {

        @Override
        public int compare(ItemsListModel o1, ItemsListModel o2) {
            return o1.getItem_name().compareTo(o2.getItem_name());
        }
    };

    public static Comparator<ItemsListModel> priceSort = new Comparator<ItemsListModel>() {

        @Override
        public int compare(ItemsListModel o1, ItemsListModel o2) {
            return o1.getItem_price().compareTo(o2.getItem_price());
        }
    };

    public static Comparator<ItemsListModel> DecPriceSort = new Comparator<ItemsListModel>() {

        @Override
        public int compare(ItemsListModel o1, ItemsListModel o2) {
            return o2.getItem_price().compareTo(o1.getItem_price());
        }
    };
}
