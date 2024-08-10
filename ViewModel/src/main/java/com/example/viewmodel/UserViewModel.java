package com.example.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.UserRepository;
import com.example.model.User;
import com.example.model.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class UserViewModel extends ViewModel {
    private MutableLiveData<Boolean> successOperation;
    private LiveData<Users> allUsers;

    private MutableLiveData<User> user;

    private UserRepository userRepository;

    public UserViewModel(Application application) {
        successOperation = new MutableLiveData<>();
        user = new MutableLiveData<>();
        userRepository = new UserRepository(application);
        allUsers = userRepository.getAll();
    }

    public void reset(){
        user = new MutableLiveData<>();
    }
    public void getUser(String id) {
        userRepository.getUserById(id)
                .addOnSuccessListener(new OnSuccessListener<User>() {
                    @Override
                    public void onSuccess(User retrievedUser) {
                        user.setValue(retrievedUser);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        user.setValue(null);
                    }
                });
    }

    public void add(User user) {
        userRepository.add(user)
                .addOnSuccessListener(aBoolean -> {
                    successOperation.setValue(true);
                })
                .addOnFailureListener(e -> {
                    successOperation.setValue(false);
                });
    }

    public void delete(String id) {
        userRepository.delete(id)
                .addOnSuccessListener(aBoolean -> {
                    successOperation.setValue(true);
                })
                .addOnFailureListener(e -> {
                    successOperation.setValue(false);
                });
    }

    public void update(User user) {
        userRepository.update(user)
                .addOnSuccessListener(aBoolean -> {
                    successOperation.setValue(true);
                })
                .addOnFailureListener(e -> {
                    successOperation.setValue(false);
                });
    }

    public void login(String email, String password){
        userRepository.login(email, password).addOnSuccessListener(new OnSuccessListener<User>() {
            @Override
            public void onSuccess(User user2) {
                user.setValue(user2);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                user.setValue(null);
            }
        });
    }

    public LiveData<Users> getAllUsers() {
        return allUsers;
    }

    public LiveData<Boolean> getSuccessOperation() {
        return successOperation;
    }

    public LiveData<User> getUserLiveData() {
        return user;
    }

    public void setUser(User user){this.user = new MutableLiveData<>(user);}
}
