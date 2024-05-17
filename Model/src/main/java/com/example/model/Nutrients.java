package com.example.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Nutrients implements Serializable {

    // needs both names 1 for edamam api (search food) and the second for spoonacular api (recipe nutritious data)
    @SerializedName(value = "CHOCDF", alternate = {"carbohydrates"})
    private Double carbs;

    @SerializedName(value = "ENERC_KCAL", alternate = {"calories"})
    private Double calories;

    @SerializedName(value = "FAT", alternate = {"fat"})
    private Double fat;

    @SerializedName(value = "FIBTG", alternate = {"fiber"})
    private Double fiber;

    @SerializedName(value = "PROCNT", alternate = {"protein"})
    private Double protein;

    public Nutrients(Double carbs, Double calories, Double fat, Double fiber, Double protein) {
        this.carbs = carbs;
        this.calories = calories;
        this.fat = fat;
        this.fiber = fiber;
        this.protein = protein;
    }

    public Nutrients(){}
    public Double getCarbs() {
        return carbs;
    }

    public void setCarbs(Double carbs) {
        this.carbs = carbs;
    }

    public Double getCalories() {
        return calories;
    }

    public void setCalories(Double calories) {
        this.calories = calories;
    }

    public Double getFat() {
        return fat;
    }

    public void setFat(Double fat) {
        this.fat = fat;
    }

    public Double getFiber() {
        return fiber;
    }

    public void setFiber(Double fiber) {
        this.fiber = fiber;
    }

    public Double getProtein() {
        return protein;
    }

    public void setProtein(Double protein) {
        this.protein = protein;
    }
}
