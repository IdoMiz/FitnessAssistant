package com.example;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class ImageRepository {

    private FirebaseStorage storage;
    private StorageReference storageRef;

    public ImageRepository(Context context) {
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference().child("images");
    }


    public Task<Boolean> add(Bitmap image, User user) {
        TaskCompletionSource<Boolean> taskCompletionSource = new TaskCompletionSource<>();

        // Generate a random UUID for the image document ID


        // Create a reference to store the image with the generated ID
        StorageReference imageRef = storageRef.child(user.getIdFs() + ".jpg");

        // Convert bitmap to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        // Upload the image
        imageRef.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Save the generated ID of the uploaded image to the user object
                taskCompletionSource.setResult(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                taskCompletionSource.setResult(false);
            }
        });

        return taskCompletionSource.getTask();
    }

    public Task<Boolean> update(String imageId, Bitmap newImage) {
        TaskCompletionSource<Boolean> taskCompletionSource = new TaskCompletionSource<>();

        // Create a reference to the updated image with the provided imageId
        StorageReference imageRef = storageRef.child(imageId + ".jpg");

        // Convert Bitmap to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        newImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageData = baos.toByteArray();

        // Upload the new image data
        imageRef.putBytes(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskCompletionSource.setResult(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                taskCompletionSource.setResult(false);
            }
        });

        return taskCompletionSource.getTask();
    }


    public Task<Uri> get(String imageId) {
        TaskCompletionSource<Uri> taskCompletionSource = new TaskCompletionSource<>();

        // Create a reference to "images/userId.jpg"
        StorageReference imageRef = storageRef.child(imageId + ".jpg");

        // Get the download URL for the image
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                taskCompletionSource.setResult(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                taskCompletionSource.setException(e);
            }
        });

        return taskCompletionSource.getTask();
    }

    public Task<Boolean> delete(String userId) {
        TaskCompletionSource<Boolean> taskCompletionSource = new TaskCompletionSource<>();

        // Create a reference to "images/userId.jpg"
        StorageReference imageRef = storageRef.child(userId + ".jpg");

        // Delete the image
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                taskCompletionSource.setResult(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                taskCompletionSource.setResult(false);
            }
        });

        return taskCompletionSource.getTask();
    }
}
