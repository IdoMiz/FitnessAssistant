package com.example.model;

import java.util.List;

public class Food {
    private int id;

    private String name;

    private String description;

    private List<Nutrient> nutrients;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Nutrient> getNutrients() {
        return nutrients;
    }
}
