package com.example.viewmodel;

import android.app.Application;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ImageRepository;
import com.example.MealRepository;
import com.example.model.Meal;
import com.example.model.Meals;
import com.example.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class ImageViewModel extends ViewModel {

    private MutableLiveData<Boolean> successOperation;
    private ImageRepository imageRepository;
    private MutableLiveData<Uri> imageMutableLiveData;

    public ImageViewModel(Application application){
        this.successOperation = new MutableLiveData<>();
        this.imageRepository = new ImageRepository(application);
        this.imageMutableLiveData = new MutableLiveData<>();
    }

    public void add(Bitmap image, User user){
        imageRepository.add(image, user).addOnSuccessListener(new OnSuccessListener<Boolean>() {
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

    public void getImage(String imageId){
        imageRepository.get(imageId).addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                imageMutableLiveData.setValue(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                imageMutableLiveData.setValue(null);
            }
        });
    }
    public void delete(String imageId) {
        imageRepository.delete(imageId).addOnSuccessListener(new OnSuccessListener<Boolean>() {
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

    public void update(String imageId, Bitmap newImage) {
        imageRepository.update(imageId, newImage).addOnSuccessListener(new OnSuccessListener<Boolean>() {
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

    public ImageRepository getImageRepository() {
        return imageRepository;
    }

    public void setImageRepository(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public MutableLiveData<Uri> getImageMutableLiveData() {
        return imageMutableLiveData;
    }

    public void setImageMutableLiveData(MutableLiveData<Uri> imageMutableLiveData) {
        this.imageMutableLiveData = imageMutableLiveData;
    }
}
