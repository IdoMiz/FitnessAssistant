package com.example.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FoodItem implements Serializable {
    @SerializedName("food")
    private Food food;


    public FoodItem(Food food) {
        this.food = food;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }
}

