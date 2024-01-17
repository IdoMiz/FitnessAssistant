package com.example.model;


import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;

public class Meal implements Serializable {
    private String id;
    private String name;
    private List<Food> foods;
    private LocalTime hour;
    private MealType type;

    public Meal(String id, String name, List<Food> foods, LocalTime hour, MealType type) {
        this.id = id;
        this.name = name;
        this.foods = foods;
        this.hour = hour;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Food> getFoods() {
        return foods;
    }

    public LocalTime getHour() {
        return hour;
    }

    public MealType getType() {
        return type;
    }

    // You can define other methods and add validation as needed
}
