package com.example.model;

import java.time.LocalDate;
import java.util.List;

public class Daily extends BaseEntity {

    private double eatenCalories;
    private double spentCalories;
    private List<Workout> workouts;
    private List<Meal> meals;
    private LocalDate date;

    // Constructor
    public Daily(String id, double eatenCalories, double spentCalories, List<Workout> workouts, List<Meal> meals, LocalDate date) {
        super(id);
        this.eatenCalories = eatenCalories;
        this.spentCalories = spentCalories;
        this.workouts = workouts;
        this.meals = meals;
        this.date = date;
    }

    // Empty constructor
    public Daily() {
        super(); // Call the constructor of the BaseEntity class
    }

    // Getter and Setter methods
    public double getEatenCalories() {
        return eatenCalories;
    }

    public void setEatenCalories(double eatenCalories) {
        this.eatenCalories = eatenCalories;
    }

    public double getSpentCalories() {
        return spentCalories;
    }

    public void setSpentCalories(double spentCalories) {
        this.spentCalories = spentCalories;
    }

    public List<Workout> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(List<Workout> workouts) {
        this.workouts = workouts;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
