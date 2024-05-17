package com.example.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Meal extends BaseEntity implements Serializable {
    private int id;
    private String title;
    private int readyInMinutes;
    private int servings;
    @SerializedName("sourceUrl")
    private String image;
    private MealEnum mealEnum;
    private String userId;
    private boolean isTemp;
    private double calories;
    private double protein;
    private double carbs;
    private double fats;

    public Meal(int id, String title, int readyInMinutes, int servings, String image, String userId, boolean isTemp) {
        this.id = id;
        this.title = title;
        this.readyInMinutes = readyInMinutes;
        this.servings = servings;
        this.image = image;
        this.userId = userId;
        this.isTemp = isTemp;

    }

    public Meal() {
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getCarbs() {
        return carbs;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    public double getFats() {
        return fats;
    }

    public void setFats(double fats) {
        this.fats = fats;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public double getCalories() {
        return calories;
    }

    public String getImage() {
        return image;
    }

    public boolean isTemp() {
        return isTemp;
    }

    public void setTemp(boolean temp) {
        isTemp = temp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setReadyInMinutes(int readyInMinutes) {
        this.readyInMinutes = readyInMinutes;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public MealEnum getMealEnum() {
        return mealEnum;
    }

    public void setMealEnum(MealEnum mealEnum) {
        this.mealEnum = mealEnum;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getReadyInMinutes() {
        return readyInMinutes;
    }

    public int getServings() {
        return servings;
    }

}
