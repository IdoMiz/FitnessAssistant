package com.example.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.FoodRepository;
import com.example.model.Food;

import com.example.model.Foods;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class FoodViewModel extends ViewModel {

    private MutableLiveData<Boolean> successOperation;
    private FoodRepository foodRepository;
    private MutableLiveData<Food> foodMutableLiveData;
    private MutableLiveData<Foods> foodsMutableLiveData;


    public FoodViewModel(Application application){
        successOperation = new MutableLiveData<>();
        foodRepository = new FoodRepository(application);
        foodMutableLiveData = new MutableLiveData<>();
        foodsMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Foods> getFoodsMutableLiveData() {
        return foodsMutableLiveData;
    }

    public void setFoodsMutableLiveData(MutableLiveData<Foods> foodsMutableLiveData) {
        this.foodsMutableLiveData = foodsMutableLiveData;
    }

    public void getFood(String id) {
        foodRepository.getFoodById(id)
                .addOnSuccessListener(new OnSuccessListener<Food>()
                {
                    @Override
                    public void onSuccess(Food retrivedFood) {
                        foodMutableLiveData.postValue(retrivedFood);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        e.printStackTrace(); // Log the error
                        foodMutableLiveData.postValue(null); // Post null to LiveData
                    }
                });
    }

    public void add(Food food){
        foodRepository.add(food).addOnSuccessListener(new OnSuccessListener<Boolean>() {
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

    public void update(Food food) {
        foodRepository.update(food)
                .addOnSuccessListener(aBoolean -> {
                    successOperation.setValue(true);
                })
                .addOnFailureListener(e -> {
                    successOperation.setValue(false);
                });
    }

    public void delete(String id) {
        foodRepository.delete(id)
                .addOnSuccessListener(aBoolean -> {
                    successOperation.setValue(aBoolean);
                })
                .addOnFailureListener(e -> {
                    successOperation.setValue(false);
                });
    }
    public void getAll(String userId){
        foodRepository.getAll(userId).addOnSuccessListener(new OnSuccessListener<Foods>() {
            @Override
            public void onSuccess(Foods foods) {
                foodsMutableLiveData.setValue(foods);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                foodMutableLiveData.setValue(null);

            }
        });

    }
//    public void fixFoodIdfs() {
//        foodRepository.fixFoodIdfs()
//                .addOnSuccessListener(new OnSuccessListener<Boolean>() {
//                    @Override
//                    public void onSuccess(Boolean result) {
//                        successOperation.setValue(result);
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        successOperation.setValue(false);
//                    }
//                });
//    }
    public void addAll(Foods foods, String userId){
        foodRepository.addAll(foods, userId).addOnSuccessListener(new OnSuccessListener<Boolean>() {
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

    public FoodRepository getFoodRepository() {
        return foodRepository;
    }

    public void setFoodRepository(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    public MutableLiveData<Food> getFoodMutableLiveData() {
        return foodMutableLiveData;
    }

    public void setFoodMutableLiveData(MutableLiveData<Food> foodMutableLiveData) {
        this.foodMutableLiveData = foodMutableLiveData;
    }

    public void removeAllByUserId(String id) {
        foodRepository.removeAllByUserId(id).addOnSuccessListener(new OnSuccessListener<Boolean>() {
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
