package com.example.model;

// Enum for gender
public enum GenderEnum {
    MALE("MALE"),
    FEMALE("MALE"),
    OTHER("OTHER");

    public String value;
    GenderEnum(String value){
        this.value = value;
    }
}
