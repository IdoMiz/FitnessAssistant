package com.example.model;

public enum MealEnum {

    BREAKFAST(0),
    LUNCH(1),
    DINNER(2);

    private final int value;

    MealEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static MealEnum getValue(int num) {
        for (MealEnum mealEnum : MealEnum.values()) {
            if (mealEnum.getValue() == num) {
                return mealEnum;
            }
        }
        return null;
    }
}
