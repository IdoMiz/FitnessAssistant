package com.example.model;


import java.io.Serializable;

public class Ingredient implements Serializable {

    private String name;
    private String unit;
    private double amount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "name='" + name  + ", amount=" + amount +", unit='" + unit;
    }
}
