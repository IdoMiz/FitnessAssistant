package com.example.model;

import com.example.helper.DateUtil;
import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User extends BaseEntity implements Serializable{
    private String name;
    private long birthDate;
    private double height;
    private double weight;
    private int daysInApp;
    @Exclude
    private Foods history;
    private String email;
    private GoalEnum goal;
    private GenderEnum gender;
    private String password;
    private ActivityLevel activityLevel;
    private long lastDayLoggedIn;
    private List<Integer> todaysMealsIds;
    private List<String> allergies;
    private DietEnum diet;
    private String profilePictureId;
    private double foodPlanCalories;
    private double foodPlanProtein;
    private double foodPlanCarbs;
    private double foodPlanFats;

    public User(String name, String email, String password, long birthDate,
                double height, double weight, GenderEnum gender, GoalEnum goal, ActivityLevel activityLevel, long lastDayLoggedIn) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.height = height;
        this.weight = weight;
        this.daysInApp = 1;
        this.history = new Foods();
        this.gender = gender;
        this.goal = goal;
        this.activityLevel = activityLevel;
        this.lastDayLoggedIn = lastDayLoggedIn;
        todaysMealsIds = new ArrayList<>();
        this.allergies = new ArrayList<>();
        this.diet = DietEnum.REGULAR;
    }
    public User(){}

    public double getFoodPlanCalories() {
        return foodPlanCalories;
    }

    public void setFoodPlanCalories(double foodPlanCalories) {
        this.foodPlanCalories = foodPlanCalories;
    }

    public double getFoodPlanProtein() {
        return foodPlanProtein;
    }

    public void setFoodPlanProtein(double foodPlanProtein) {
        this.foodPlanProtein = foodPlanProtein;
    }

    public double getFoodPlanCarbs() {
        return foodPlanCarbs;
    }

    public void setFoodPlanCarbs(double foodPlanCarbs) {
        this.foodPlanCarbs = foodPlanCarbs;
    }

    public double getFoodPlanFats() {
        return foodPlanFats;
    }

    public void setFoodPlanFats(double foodPlanFats) {
        this.foodPlanFats = foodPlanFats;
    }

    public String getProfilePictureId() {
        return profilePictureId;
    }

    public void setProfilePictureId(String profilePictureId) {
        this.profilePictureId = profilePictureId;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public String getStringAllergies(){

        String items = "";
        for(String item : allergies){
            if(items.isEmpty()){
                items+=item;
            }
            else{
                items += (","+ item);
            }

        }

        return items;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }

    public DietEnum getDiet() {
        return diet;
    }

    public void setDiet(DietEnum diet) {
        this.diet = diet;
    }

    public List<Integer> getTodaysMealsIds() {
        return todaysMealsIds;
    }

    public void setTodaysMealsIds(List<Integer> todaysMealsIds) {
        this.todaysMealsIds = todaysMealsIds;
    }

    public long getLastDayLoggedIn() {
        return lastDayLoggedIn;
    }

    public void setLastDayLoggedIn(long lastDayLoggedIn) {
        this.lastDayLoggedIn = lastDayLoggedIn;
    }

    public ActivityLevel getActivityLevel() {

        return activityLevel;
    }

    public void setActivityLevel(ActivityLevel activityLevel) {
        this.activityLevel = activityLevel;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    @Exclude
    public Foods getHistory() {
        return history;
    }

    @Exclude
    public void setHistory(Foods foods) {
        this.history = foods;
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

    public void addFood(Food food) {
        boolean notFound = true;

        if (history == null) {
            history = new Foods();
            history.add(food);
            return;
        }

        for (Food foodItem : history) {
            if (foodItem != null && foodItem.equals(food)) {
                history.set(history.indexOf(foodItem), food);
                notFound = false;
                break;
            }
        }

        if (notFound) {
            history.add(food);
        }
    }

    public void addFoods(Foods foods1){
        if(history == null){
            history = new Foods();
        }
        history.addAll(foods1);
    }

    public void removeFood(Food food){
        if(history == null){
            history = new Foods();
        }
        history.remove(food);
    }

    public double getConsumedCalories(long date){
        Foods foods = getDay(date);
        double sum = 0;

        for(Food food : foods){
            if(food != null){
                sum += food.getNutrients().getCalories()* food.getServing();
            }
        }

        return sum;

    }
    public Foods getBreakfast(long date){
        Foods temp = getDay(date);
        Foods breakfast = new Foods();

        for (Food food: temp) {
            if(food.getMeal() == MealEnum.BREAKFAST){
                breakfast.add(food);
            }
        }
        return breakfast;
    }
    public Foods getLunch(long date){
        Foods temp = getDay(date);
        Foods lunch = new Foods();

        for (Food food: temp) {
            if(food.getMeal() == MealEnum.LUNCH){
                lunch.add(food);
            }
        }
        return lunch;
    }
    public Foods getDinner(long date){
        Foods temp = getDay(date);
        Foods dinner = new Foods();

        for (Food food: temp) {
            if(food.getMeal() == MealEnum.DINNER){
                dinner.add(food);
            }
        }
        return dinner;
    }

    public int getRecommendedCalories() {
        if(activityLevel == null){
            return 0;
        }
        double BMR =0;
        int age = LocalDate.now().getYear() - DateUtil.longToLocalDate(this.birthDate).getYear();

        if (this.gender == GenderEnum.MALE) {
            BMR = 88.362 + (13.397 * this.weight) + (4.799 * this.height) - (5.677 * age);
        } else {
            BMR = 447.593 + (9.247 * this.weight) + (3.098 * this.height) - (4.330 * age);
        }

        double caloriesMultiplier = this.activityLevel.getMultiplier();
        if(goal == GoalEnum.MAINTAIN_WEIGHT){
            return (int) (BMR * caloriesMultiplier);
        } else if (goal == GoalEnum.GAIN_WEIGHT) {
            return (int) (BMR * caloriesMultiplier*1.1);
        }
        else {
            return (int) (BMR * caloriesMultiplier*0.9);
        }
    }
    public Foods getDay(long date){
        Foods foods = new Foods();
        if(history== null){
            return foods;
        }
        for(Food food :history){
            if(food != null && food.getDate() == date){
                foods.add(food);
            }
        }
        return foods;
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
                gender == user.gender&&
                Objects.equals(password, user.password);
    }
}
