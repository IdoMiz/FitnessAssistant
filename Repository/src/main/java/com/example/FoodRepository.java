package com.example;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.model.Food;

import com.example.model.Foods;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FoodRepository {

    private FirebaseFirestore db;
    private CollectionReference collection;
    private MutableLiveData<Food> foodMutableLiveData;

    public FoodRepository(Context context){
        foodMutableLiveData = new MutableLiveData<>();
        try {
            db = FirebaseFirestore.getInstance();
        }catch (Exception e){
            FirebaseInstance instance = FirebaseInstance.instance(context);
            db = FirebaseFirestore.getInstance(FirebaseInstance.app);
        }
        collection = db.collection("Foods");
    }

    public Task<Boolean> add(Food food){
        TaskCompletionSource<Boolean> taskCompletionSource = new TaskCompletionSource<>();

        DocumentReference document = collection.document();

        food.setIdFs(document.getId());
        document.set(food).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
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
    public Task<Boolean> removeAllByUserId(String userId) {
        TaskCompletionSource<Boolean> taskCompletionSource = new TaskCompletionSource<>();

        collection.whereEqualTo("userId", userId).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Task<Void>> deleteTasks = new ArrayList<>();
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                            DocumentReference document = snapshot.getReference();
                            deleteTasks.add(document.delete());
                        }

                        // Wait for all delete tasks to complete
                        Tasks.whenAll(deleteTasks)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        taskCompletionSource.setResult(true);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        taskCompletionSource.setResult(false);
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        taskCompletionSource.setResult(false);
                    }
                });

        return taskCompletionSource.getTask();
    }

    public Task<Foods> getAll(String userId){
        TaskCompletionSource<Foods> task = new TaskCompletionSource<>();

        collection = db.collection("Foods");

        collection.whereEqualTo("userId", userId).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Foods foods = new Foods();
                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                                Food fetchedFood = snapshot.toObject(Food.class);

                                foods.add(fetchedFood);

                            }
                        }
                        task.setResult(foods);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        task.setResult(null);
                    }
                });

        return task.getTask();
    }

//    public Task<Boolean> fixFoodIdfs() {
//        TaskCompletionSource<Boolean> taskCompletion = new TaskCompletionSource<>();
//
//        collection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                List<Task<Void>> updateTasks = new ArrayList<>();
//                for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
//                    Food food = snapshot.toObject(Food.class);
//                    if (food != null) {
//                        food.setIdFs(snapshot.getId());
//                        updateTasks.add(snapshot.getReference().set(food));
//                    }
//                }
//
//                // Wait for all update tasks to complete
//                Tasks.whenAll(updateTasks)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                taskCompletion.setResult(true);
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                taskCompletion.setResult(false);
//                            }
//                        });
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                taskCompletion.setResult(false);
//            }
//        });
//
//        return taskCompletion.getTask();
//    }
    public Task<Boolean> addAll(Foods foods, String userId) {
        TaskCompletionSource<Boolean> taskCompletion = new TaskCompletionSource<>();

        for (Food food : foods) {
            DocumentReference document = collection.document();

            food.setIdFs(document.getId());
            food.setUserId(userId);
            collection.add(food)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Error occurred while adding this user food item
                            // Set overall task completion to false and exit loop
                            taskCompletion.setResult(false);
                        }
                    });
        }

        // All user food items added successfully
        taskCompletion.setResult(true);

        return taskCompletion.getTask();
    }

    public Task<Food> getFoodById(String id){
        TaskCompletionSource<Food> task = new TaskCompletionSource<>();

        collection.document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                task.setResult(documentSnapshot.toObject(Food.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                task.setResult(null);
            }
        });

        return task.getTask();
    }

    public Task<Boolean> update(@NonNull Food food) {
        TaskCompletionSource<Boolean> taskCompletion = new TaskCompletionSource<>();

        DocumentReference document = collection.document(food.getIdFs());

        document.set(food)
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

    public Task<Boolean> delete(String foodId) {
        TaskCompletionSource<Boolean> taskCompletion = new TaskCompletionSource<>();

        DocumentReference document = collection.document(foodId);

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
}
