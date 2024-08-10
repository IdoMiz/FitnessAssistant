package com.example;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.model.Meal;
import com.example.model.Meals;
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

public class MealRepository {

    private FirebaseFirestore db;
    private CollectionReference collection;

    public MealRepository(Context context){
        try {
            db = FirebaseFirestore.getInstance();
        }catch (Exception e){
            FirebaseInstance instance = FirebaseInstance.instance(context);
            db = FirebaseFirestore.getInstance(FirebaseInstance.app);
        }
        collection = db.collection("Meals");
    }
    public Task<Meals> getTempoMeals(String userId) {
        TaskCompletionSource<Meals> taskCompletionSource = new TaskCompletionSource<>();

        collection.whereEqualTo("temp", true).whereEqualTo("userId", userId).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Meals tempoMeals = new Meals();
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                            Meal meal = snapshot.toObject(Meal.class);
                            tempoMeals.add(meal);
                        }
                        taskCompletionSource.setResult(tempoMeals);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        taskCompletionSource.setException(e);
                    }
                });

        return taskCompletionSource.getTask();
    }


    public Task<Boolean> add(Meal meal){
        TaskCompletionSource<Boolean> taskCompletionSource = new TaskCompletionSource<>();

        DocumentReference document = collection.document();

        meal.setIdFs(document.getId());
        document.set(meal).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    public Task<Meals> getAll(String userId){
        TaskCompletionSource<Meals> task = new TaskCompletionSource<>();

        collection.whereEqualTo("userId", userId).whereEqualTo("temp", false).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Meals meals = new Meals();
                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                                Meal fetchedMeal = snapshot.toObject(Meal.class);
                                meals.add(fetchedMeal);
                            }
                        }
                        task.setResult(meals);
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

    public Task<Boolean> addAll(Meals meals, String userId) {
        TaskCompletionSource<Boolean> taskCompletion = new TaskCompletionSource<>();

        for (Meal meal : meals) {
            DocumentReference document = collection.document(); // Generate a new document reference for each meal
            meal.setIdFs(document.getId());
            meal.setUserId(userId);
            document.set(meal)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Success
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            taskCompletion.setResult(false);
                        }
                    });
        }

        // All user meal items added successfully
        taskCompletion.setResult(true);

        return taskCompletion.getTask();
    }

    public Task<Boolean> removeTempoField(String userId) {
        TaskCompletionSource<Boolean> taskCompletionSource = new TaskCompletionSource<>();

        collection.whereEqualTo("temp", true).whereEqualTo("userId", userId).get()
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

    public Task<Meal> getMealById(int id){
        TaskCompletionSource<Meal> task = new TaskCompletionSource<>();

        collection.whereEqualTo("id", id).whereEqualTo("temp", false).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                    Meal meal = documentSnapshot.toObject(Meal.class);
                    task.setResult(meal);
                } else {
                    task.setResult(null);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                task.setException(e);
            }
        });

        return task.getTask();
    }

    public Task<Boolean> update(@NonNull Meal meal) {
        TaskCompletionSource<Boolean> taskCompletion = new TaskCompletionSource<>();

        DocumentReference document = collection.document(meal.getIdFs());

        document.set(meal)
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

    public Task<Boolean> delete(int id, boolean isTempo) {
        TaskCompletionSource<Boolean> taskCompletionSource = new TaskCompletionSource<>();

        collection.whereEqualTo("id", id)
                .whereEqualTo("temp", isTempo)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            documentSnapshot.getReference()
                                    .delete()
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
                        } else {
                            taskCompletionSource.setResult(false); // Document not found or isTemp is true
                        }
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

}
