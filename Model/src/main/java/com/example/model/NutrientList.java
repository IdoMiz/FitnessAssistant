package com.example.model;

import java.io.Serializable;
import java.util.List;

public class NutrientList implements Serializable {


    private List<SingleNutrient> nutrients;
    public List<SingleNutrient> getNutrients() {
        return nutrients;
    }
    public void setNutrients(List<SingleNutrient> nutrients) {
        this.nutrients = nutrients;
    }
}