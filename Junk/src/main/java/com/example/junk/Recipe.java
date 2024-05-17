package com.example.junk;


import java.io.Serializable;

public class Recipe implements Serializable {
    private String name;

    public Recipe(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "name='" + name + '\'' +
                '}';
    }
}