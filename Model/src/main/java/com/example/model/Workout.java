package com.example.model;

import java.io.Serializable;

public class Workout implements Serializable {
    private String id;
    private String activityName;
    private int duration;
    private ActivityType activityType;

    public Workout(String id, String activityName, int duration, ActivityType activityType) {
        this.id = id;
        this.activityName = activityName;
        this.duration = duration;
        this.activityType = activityType;
    }

    public String getId() {
        return id;
    }

    public String getActivityName() {
        return activityName;
    }

    public int getDuration() {
        return duration;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

}