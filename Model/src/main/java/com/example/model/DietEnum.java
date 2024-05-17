package com.example.model;

public enum DietEnum {
    REGULAR("Regular", "Eating all"),
    VEGAN("Vegan", "No meat, dairy, eggs, or honey"),
    PALEO("Paleo", "No grains, dairy, or processed foods"),
    PRIMAL("Primal", "Similar to Paleo, but allows dairy"),
    KETOGENIC("Ketogenic", "High fat, moderate protein, low carb"),
    WHOLE30("Whole30", "No added sugars, grains, dairy, or legumes"),
    GLUTEN_FREE("Gluten Free", "Avoid wheat, barley, and rye"),
    VEGETARIAN("Vegetarian", "No meat or fish"),
    LACTO_VEGETARIAN("Lacto-Vegetarian", "Vegetarian, no eggs"),
    OVO_VEGETARIAN("Ovo-Vegetarian", "Vegetarian, no dairy"),
    PESCETARIAN("Pescetarian", "No meat, but allows fish"),
    LOW_FODMAP("Low FODMAP", "Reduces certain carbohydrates");
    private final String name;
    private final String description;

    DietEnum(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
