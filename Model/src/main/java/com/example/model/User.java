package com.example.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User extends BaseEntity {

    private String name;
    private long birthDate;  // Changed to long
    private double height;
    private double weight;
    private int daysInApp;
    private List<Daily> history;
    private String email;
    private GoalEnum goal;
    private GenderEnum gender;

    // Constructor including the idFs from the BaseEntity
    public User(String idFs, String name, String birthDate, String height, String weight,
                String email, double goal, double gender) {
        super(idFs);
        this.name = name;
        this.birthDate = birthDate;
        this.height = height;
        this.weight = weight;
        this.daysInApp = 1;
        this.history = new ArrayList<>();
        this.email = email;
        this.goal = goal;
        this.gender = gender;
    }

    // Getter and Setter methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(long birthDate) {
        this.birthDate = birthDate;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getDaysInApp() {
        return daysInApp;
    }

    public void setDaysInApp(int daysInApp) {
        this.daysInApp = daysInApp;
    }

    public List<Daily> getHistory() {
        return history;
    }

    public void setHistory(List<Daily> history) {
        this.history = history;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public GoalEnum getGoal() {
        return goal;
    }

    public void setGoal(GoalEnum goal) {
        this.goal = goal;
    }

    public GenderEnum getGender() {
        return gender;
    }

    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return daysInApp == user.daysInApp &&
                Double.compare(user.height, height) == 0 &&
                Double.compare(user.weight, weight) == 0 &&
                Objects.equals(name, user.name) &&
                birthDate == user.birthDate &&  // Compare long values
                Objects.equals(history, user.history) &&
                Objects.equals(email, user.email) &&
                goal == user.goal &&
                gender == user.gender;
    }
}
