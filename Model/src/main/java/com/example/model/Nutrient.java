package com.example.model;

import java.io.Serializable;

public class Nutrient implements Serializable {
    private String name;
    private double amount;
    private String unit;

    // Add any additional nutrient-related fields based on the API response

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public String getUnit() {
        return unit;
    }
}

