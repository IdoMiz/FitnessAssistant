package com.example.junk;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.model.Daily;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class DailyRepository {

    private FirebaseFirestore db;
    private CollectionReference collection;

    private final MutableLiveData<Dailies> dailiesLiveData;

    public DailyRepository(Context context){
        dailiesLiveData = new MutableLiveData<>();
        try {
            db = FirebaseFirestore.getInstance();
        }
        catch (Exception e){
            FirebaseInstance instance =
                    FirebaseInstance.instance(context);

            db = FirebaseFirestore
                    .getInstance(FirebaseInstance.app);
        }

        collection = db.collection("Dailies");
    }
    public DailyRepository(MutableLiveData<Dailies> dailiesLiveData1){
        dailiesLiveData = dailiesLiveData1;
    }



    public Task<Boolean> add(Daily daily) {
        TaskCompletionSource<Boolean> taskCompletion = new TaskCompletionSource<>();

        DocumentReference document = collection.document();
        // Set the document ID from Firestore
        daily.setIdFs(document.getId());
        document.set(daily)
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

    public Task<Boolean> update(@NonNull Daily daily) {
        TaskCompletionSource<Boolean> taskCompletion = new TaskCompletionSource<>();

        DocumentReference document = collection.document(daily.getIdFs());

        document.set(daily)
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

    public Task<Boolean> delete(String postId) {
        TaskCompletionSource<Boolean> taskCompletion = new TaskCompletionSource<>();

        DocumentReference document = collection.document(postId);

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

    public MutableLiveData<Dailies> getAll() {
        Dailies dailies = new Dailies();

        collection.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            for (DocumentSnapshot document : querySnapshot) {
                                Daily daily = document.toObject(Daily.class);
                                if (daily != null) {
                                    dailies.add(daily);
                                }
                            }
                        }
                        dailiesLiveData.postValue(dailies);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dailiesLiveData.postValue(dailies);
                    }
                });

        return dailiesLiveData;
    }

}
