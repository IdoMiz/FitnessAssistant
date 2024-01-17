package com.example.model;

// Enum for goals
public enum GoalEnum {
    LOSE_WEIGHT,
    MAINTAIN_WEIGHT,
    GAIN_WEIGHT;

    public static GoalEnum fromString(String selectedGoal) {
        switch (selectedGoal) {
            case "Lose Weight":
                return LOSE_WEIGHT;
            case "Maintain Weight":
                return MAINTAIN_WEIGHT;
            case "Gain Weight":
                return GAIN_WEIGHT;
            default:
                throw new IllegalArgumentException("Unknown goal: " + selectedGoal);
        }
    }
}
