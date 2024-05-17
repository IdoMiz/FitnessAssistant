package com.example.model;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.List;

public class MealPlanResponse implements Serializable {
    private List<Meal> meals;
    private Nutrients nutrients;

    public List<Meal> getMeals() {
        return meals;
    }
    public Nutrients getNutrients() {
        return nutrients;
    }
}