package com.example;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.model.User;
import com.example.model.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class UserRepository {

    private FirebaseFirestore db;
    private CollectionReference collection;

    private final MutableLiveData<Users> usersLiveData;

    public UserRepository(Context context){
        usersLiveData = new MutableLiveData<>();
        try {
            db = FirebaseFirestore.getInstance();
        }
        catch (Exception e){
            FirebaseInstance instance = FirebaseInstance.instance(context);
            db = FirebaseFirestore.getInstance(FirebaseInstance.app);
        }

        collection = db.collection("Users");
    }

    public UserRepository(MutableLiveData<Users> usersLiveData1){
        usersLiveData = usersLiveData1;
    }

    public Task<Boolean> add(User user) {
        TaskCompletionSource<Boolean> taskCompletion = new TaskCompletionSource<>();

        DocumentReference document = collection.document();
        // Set the document ID from Firestore
        user.setIdFs(document.getId());
        document.set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        taskCompletion.setResult(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        taskCompletion.setResult(false); // Set the exception for better error handling
                    }
                });

        return taskCompletion.getTask();
    }

    public Task<Boolean> update(@NonNull User user) {
        TaskCompletionSource<Boolean> taskCompletion = new TaskCompletionSource<>();

        DocumentReference document = collection.document(user.getIdFs());

        document.set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        taskCompletion.setResult(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        taskCompletion.setResult(false);
                    }
                });

        return taskCompletion.getTask();
    }

    public Task<Boolean> delete(String userId) {
        TaskCompletionSource<Boolean> taskCompletion = new TaskCompletionSource<>();

        DocumentReference document = collection.document(userId);

        document.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        taskCompletion.setResult(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        taskCompletion.setResult(false);
                    }
                });

        return taskCompletion.getTask();
    }

    public MutableLiveData<Users> getAll() {
        Users users = new Users();

        collection.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            for (DocumentSnapshot document : querySnapshot) {
                                User user = document.toObject(User.class);
                                if (user != null) {
                                    users.add(user);
                                }
                            }
                        }
                        usersLiveData.postValue(users);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        usersLiveData.postValue(users);
                    }
                });

        return usersLiveData;
    }
}
