package com.example.model;

public enum ActivityLevel {
    PICK_ACTIVITY_LEVEL(0),
    SEDENTARY(1.2), // little or no exercise
    LIGHTLY_ACTIVE(1.375), // light exercise/sports 1-3 days/week
    MODERATELY_ACTIVE(1.55), // moderate exercise/sports 3-5 days/week
    VERY_ACTIVE(1.725), // hard exercise/sports 6-7 days a week
    EXTREMELY_ACTIVE(1.9); // very hard exercise/sports & physical job or 8+ workouts a week

    private final double multiplier;

    ActivityLevel(double multiplier) {
        this.multiplier = multiplier;
    }

    public double getMultiplier() {
        return multiplier;
    }

}
