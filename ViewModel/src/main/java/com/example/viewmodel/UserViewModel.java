package com.example.viewmodel;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.UserRepository;  // Assuming you have a UserRepository
import com.example.model.Users;    // Assuming you have a Users model
import com.example.model.User;     // Assuming you have a User model

public class UserViewModel extends ViewModel {
    private MutableLiveData<Boolean> successOperation;
    private LiveData<Users> allUsers;  // Change type to LiveData<Users>

    private UserRepository userRepository;

    public UserViewModel(Application application) {
        successOperation = new MutableLiveData<>();

        userRepository = new UserRepository(application); // Initialize your repository here
        allUsers = userRepository.getAll(); // Assign LiveData from repository
    }

    public void add(User user) {  // Change parameter type to User
        userRepository.add(user)
                .addOnSuccessListener(aBoolean -> {
                    successOperation.setValue(true); // Set the result directly
                })
                .addOnFailureListener(e -> {
                    successOperation.setValue(false); // Set a failure value
                });
    }

    public void update(User user) {  // Change parameter type to User
        userRepository.update(user)
                .addOnSuccessListener(aBoolean -> {
                    successOperation.setValue(true); // Set the result directly
                })
                .addOnFailureListener(e -> {
                    successOperation.setValue(false); // Set a failure value
                });
    }

    public void delete(String id) {
        userRepository.delete(id)
                .addOnSuccessListener(aBoolean -> {
                    successOperation.setValue(true); // Set the result directly
                })
                .addOnFailureListener(e -> {
                    successOperation.setValue(false); // Set a failure value
                });
    }

    public LiveData<Users> getAllUsers() {
        return allUsers;
    }

    public LiveData<Boolean> getSuccessOperation() {
        return successOperation;
    }
}
