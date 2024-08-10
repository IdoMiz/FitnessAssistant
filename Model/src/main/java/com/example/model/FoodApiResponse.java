package com.example.model;

import java.io.Serializable;
import java.util.List;

public class FoodApiResponse implements Serializable {
    private List<FoodItem> parsed;
    private List<FoodItem> hints;
    public FoodApiResponse(List<FoodItem> parsed, List<FoodItem> hints) {
        this.parsed = parsed;
        this.hints = hints;

    }

    public List<FoodItem> getParsed() {
        return parsed;
    }

    public void setParsed(List<FoodItem> parsed) {
        this.parsed = parsed;
    }

    public List<FoodItem> getHints() {
        return hints;
    }

    public void setHints(List<FoodItem> hints) {
        this.hints = hints;
    }

    @Override
    public String toString() {
        return "FoodSearchResponse{" +
                "parsed=" + parsed +
                ", hints=" + hints + '}';
    }
}
