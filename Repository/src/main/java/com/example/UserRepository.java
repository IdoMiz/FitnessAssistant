package com.example;

import android.content.Context;
import android.util.Log;

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

    private final MutableLiveData<Users> usersMutableLiveData;
    private MutableLiveData<User> userMutableLiveData;

    public UserRepository(Context context){
        usersMutableLiveData = new MutableLiveData<>();
        userMutableLiveData = new MutableLiveData<>();
        try {
            db = FirebaseFirestore.getInstance();
        }
        catch (Exception e){
            FirebaseInstance instance = FirebaseInstance.instance(context);
            db = FirebaseFirestore.getInstance(FirebaseInstance.app);
        }

        collection = db.collection("Users");
    }

    public Task<Boolean> add(User user) {
        TaskCompletionSource<Boolean> taskCompletion = new TaskCompletionSource<>();

        DocumentReference document = collection.document();

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
                        taskCompletion.setResult(false);
                    }
                });

        return taskCompletion.getTask();
    }
    public Task<User> getUserById(String userId) {
        TaskCompletionSource<User> completionSource = new TaskCompletionSource<>();


        collection
                .document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            User user = documentSnapshot.toObject(User.class);
                            completionSource.setResult(user);
                        } else {
                            completionSource.setResult(null); // No user found
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completionSource.setResult(null);
                    }
                });
        return completionSource.getTask();
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
                        usersMutableLiveData.setValue(users);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        usersMutableLiveData.setValue(users);
                    }
                });

        return usersMutableLiveData;
    }

    public Task<User> login(String email, String password) {

        TaskCompletionSource<User> taskCompletionSource = new TaskCompletionSource<>();
        collection.whereEqualTo("email", email).whereEqualTo("password",password).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            User user = documentSnapshot.toObject(User.class);
                            taskCompletionSource.setResult(user);
                        } else {
                            taskCompletionSource.setResult(null);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("LoginFailure", "Login failed with exception", e);

                        taskCompletionSource.setResult(null);
                        taskCompletionSource.setException(e);
                    }
                });
        return taskCompletionSource.getTask();
    }
}
