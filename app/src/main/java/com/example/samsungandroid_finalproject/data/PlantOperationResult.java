package com.example.samsungandroid_finalproject.data;

public class PlantOperationResult<T> {
    private final T data;

    private final boolean success;

    private final String errorMessage;

    public PlantOperationResult(T data, boolean success, String errorMessage) {
        this.data = data;
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public T getData() {
        return data;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
