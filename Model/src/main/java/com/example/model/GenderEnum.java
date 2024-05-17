package com.example.model;

// Enum for gender
public enum GenderEnum {
    MALE("MALE"),
    FEMALE("FEMALE");

    public String value;
    GenderEnum(String value){
        this.value = value;
    }
}
