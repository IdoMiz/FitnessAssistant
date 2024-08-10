package com.example.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.MealRepository;
import com.example.model.Meal;

import com.example.model.Meals;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class MealViewModel extends ViewModel {

    private MutableLiveData<Boolean> successOperation;
    private MealRepository mealRepository;
    private MutableLiveData<Meal> mealMutableLiveData;
    private MutableLiveData<Meals> mealsMutableLiveData;


    public MealViewModel(Application application){
        successOperation = new MutableLiveData<>();
        mealRepository = new MealRepository(application);
        mealMutableLiveData = new MutableLiveData<>();
        mealsMutableLiveData = new MutableLiveData<>();
    }

    public void getTempoMeals(String userId) {
        mealRepository.getTempoMeals(userId)
                .addOnSuccessListener(new OnSuccessListener<Meals>() {
                    @Override
                    public void onSuccess(Meals meals) {
                        mealsMutableLiveData.setValue(meals);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mealsMutableLiveData.setValue(null);
                    }
                });
    }

    public MutableLiveData<Meals> getMealsMutableLiveData() {
        return mealsMutableLiveData;
    }

    public void setMealsMutableLiveData(MutableLiveData<Meals> mealsMutableLiveData) {
        this.mealsMutableLiveData = mealsMutableLiveData;
    }

    public void removeTempoFields(String userId){
        mealRepository.removeTempoField(userId).addOnSuccessListener(new OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                successOperation.setValue(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                successOperation.setValue(false);
            }
        });
    }

    public void getMeal(int id) {
        mealRepository.getMealById(id)
                .addOnSuccessListener(new OnSuccessListener<Meal>()
                {
                    @Override
                    public void onSuccess(Meal retrievedMeal) {
                        mealMutableLiveData.postValue(retrievedMeal);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        e.printStackTrace(); // Log the error
                        mealMutableLiveData.postValue(null); // Post null to LiveData
                    }
                });
    }

    public void add(Meal meal){
        mealRepository.add(meal).addOnSuccessListener(new OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                successOperation.setValue(aBoolean);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                successOperation.setValue(false);
            }
        });
    }

    public void update(Meal meal) {
        mealRepository.update(meal)
                .addOnSuccessListener(aBoolean -> {
                    successOperation.setValue(true);
                })
                .addOnFailureListener(e -> {
                    successOperation.setValue(false);
                });
    }

    public void delete(int id, boolean isTempo) {
        mealRepository.delete(id, isTempo)
                .addOnSuccessListener(aBoolean -> {
                    successOperation.setValue(true);
                })
                .addOnFailureListener(e -> {
                    successOperation.setValue(false);
                });
    }
    public void getAll(String userId){
        mealRepository.getAll(userId).addOnSuccessListener(new OnSuccessListener<Meals>() {
            @Override
            public void onSuccess(Meals meals) {
                mealsMutableLiveData.setValue(meals);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mealMutableLiveData.setValue(null);

            }
        });

    }
    public void addAll(Meals meals, String userId){
        mealRepository.addAll(meals, userId).addOnSuccessListener(new OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                successOperation.setValue(aBoolean);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                successOperation.setValue(false);
            }
        });
    }

    public MutableLiveData<Boolean> getSuccessOperation() {
        return successOperation;
    }

    public void setSuccessOperation(MutableLiveData<Boolean> successOperation) {
        this.successOperation = successOperation;
    }

    public MealRepository getMealRepository() {
        return mealRepository;
    }

    public void setMealRepository(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    public MutableLiveData<Meal> getMealMutableLiveData() {
        return mealMutableLiveData;
    }

    public void setMealMutableLiveData(MutableLiveData<Meal> mealMutableLiveData) {
        this.mealMutableLiveData = mealMutableLiveData;
    }

    public void removeAllByUserId(String id) {
        mealRepository.removeAllByUserId(id).addOnSuccessListener(new OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                successOperation.setValue(aBoolean);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                successOperation.setValue(false);
            }
        });
    }

}
