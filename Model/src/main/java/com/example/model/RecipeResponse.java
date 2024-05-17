package com.example.model;

import java.io.Serializable;
import java.util.List;

public class RecipeResponse extends BaseEntity implements Serializable {
    private int id;
    private String title;
    private String image;
    private int servings;
    private int readyInMinutes;
    private String summary;
    private List<Ingredient> extendedIngredients;
    private NutrientList nutrition;

    public NutrientList getNutrient() {
        return nutrition;
    }

    public double getCalories(){
        List<SingleNutrient> nutrientList = nutrition.getNutrients();

        for(SingleNutrient singleNutrient : nutrientList){
            if(singleNutrient.getName().equals("Calories")){
                return singleNutrient.getAmount();
            }
        }

        return 0;
    }

    public double getProteins(){
        List<SingleNutrient> nutrientList = nutrition.getNutrients();

        for(SingleNutrient singleNutrient : nutrientList){
            if(singleNutrient.getName().equals("Protein")){
                return singleNutrient.getAmount();
            }
        }

        return 0;
    }

    public double getCarbs(){
        List<SingleNutrient> nutrientList = nutrition.getNutrients();

        for(SingleNutrient singleNutrient : nutrientList){
            if(singleNutrient.getName().equals("Carbohydrates")){
                return singleNutrient.getAmount();
            }
        }

        return 0;
    }

    public double getFats(){
        List<SingleNutrient> nutrientList = nutrition.getNutrients();

        for(SingleNutrient singleNutrient : nutrientList){
            if(singleNutrient.getName().equals("Fat")){
                return singleNutrient.getAmount();
            }
        }

        return 0;
    }
    public void setNutrient(NutrientList nutrientList) {
        this.nutrition = nutrientList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public void setReadyInMinutes(int readyInMinutes) {
        this.readyInMinutes = readyInMinutes;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<Ingredient> getExtendedIngredients() {
        return extendedIngredients;
    }

    public void setExtendedIngredients(List<Ingredient> extendedIngredients) {
        this.extendedIngredients = extendedIngredients;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public int getServings() {
        return servings;
    }

    public int getReadyInMinutes() {
        return readyInMinutes;
    }

    public String getSummary() {
        return summary;
    }
}