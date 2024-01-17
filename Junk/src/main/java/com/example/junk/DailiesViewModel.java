package com.example.junk;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.model.Daily;

public class DailiesViewModel extends ViewModel {
    private MutableLiveData<Boolean> successOperation;
    private LiveData<Dailies> allDailies;

    private DailyRepository repository;

    public DailiesViewModel(Application application) {
        successOperation = new MutableLiveData<>();

        repository = new DailyRepository(application); // Initialize your repository here
        allDailies = repository.getAll(); // Assign LiveData from repository
    }

    public void add(Daily daily) {  // Change parameter type to Daily
        repository.add(daily)
                .addOnSuccessListener(aBoolean -> {
                    successOperation.setValue(true); // Set the result directly
                })
                .addOnFailureListener(e -> {
                    successOperation.setValue(false); // Set a failure value
                });
    }

    public void update(Daily daily) {  // Change parameter type to Daily
        repository.update(daily)
                .addOnSuccessListener(aBoolean -> {
                    successOperation.setValue(true); // Set the result directly
                })
                .addOnFailureListener(e -> {
                    successOperation.setValue(false); // Set a failure value
                });
    }

    public void delete(String id) {
        repository.delete(id)
                .addOnSuccessListener(aBoolean -> {
                    successOperation.setValue(true); // Set the result directly
                })
                .addOnFailureListener(e -> {
                    successOperation.setValue(false); // Set a failure value
                });
    }

    public LiveData<Dailies> getAllDailies() {
        return allDailies;
    }

    public LiveData<Boolean> getSuccessOperation() {
        return successOperation;
    }
}
