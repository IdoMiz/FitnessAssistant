package com.example.model;

import com.google.firebase.firestore.Exclude;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Objects;

public class Food extends BaseEntity implements Serializable {

    @SerializedName("category")
    private String category;

    @SerializedName("image")
    private String image;

    @SerializedName("label")
    private String label;

    @SerializedName("nutrients")
    private Nutrients nutrients;
    //private List<Measure> measures;
    private long date;
    private MealEnum meal;
    @Exclude
    private LocalTime time;
    private double serving = 1;
    private String userId;

    public Food(String image, String label, long date, MealEnum meal, double serving, String userId, Nutrients nutrients) {
        this.image = image;
        this.label = label;
        this.date = date;
        this.meal = meal;
        this.serving = serving;
        this.userId = userId;
        this.nutrients = nutrients;
    }

    public Food() {}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    @Exclude
    public LocalTime getTime() {
        return time;
    }
    @Exclude
    public void setTime(LocalTime time) {
        this.time = time;
    }

    public MealEnum getMeal() {
        return meal;
    }

    public void setMeal(MealEnum meal) {
        this.meal = meal;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public double getServing() {
        return serving;
    }

    public void setServing(double serving) {
        this.serving = serving;
    }
/*
    public List<Measure> getMeasures() {
        return measures;
    }

    public void setMeasures(List<Measure> measures) {
        this.measures = measures;
    }
*/
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Nutrients getNutrients() {
        return nutrients;
    }

    public void setNutrients(Nutrients nutrients) {
        this.nutrients = nutrients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Food food = (Food) o;
        return time.equals(((Food) o).time)&& date == food.date && Double.compare(food.serving, serving) == 0 && Objects.equals(category, food.category) && Objects.equals(image, food.image) && Objects.equals(label, food.label) && Objects.equals(nutrients, food.nutrients) && meal == food.meal;
    }

}
