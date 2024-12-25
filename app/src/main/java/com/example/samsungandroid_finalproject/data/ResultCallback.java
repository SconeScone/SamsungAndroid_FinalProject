package com.example.samsungandroid_finalproject.data;

public interface ResultCallback<T> {
    void onComplete(PlantOperationResult<T> result);
}
