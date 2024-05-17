package com.example.model;

// needs to implement the goal to the recommended calories
public enum GoalEnum {
    PICK_GOAL("Pick Goal"),
    LOSE_WEIGHT("Lose Weight"),
    MAINTAIN_WEIGHT("Maintain Weight"),
    GAIN_WEIGHT("Gain Weight");

    public final String displayGoal;

    GoalEnum(String displayGoal) {
        this.displayGoal = displayGoal;
    }

    public String getDisplayName() {
        return displayGoal;
    }

    @Override
    public String toString() {
        return displayGoal;
    }
}
