package com.example.samsungandroid_finalproject.domain;

public class ValidationResult {
    private final boolean valid;
    private final String errorMessage;

    public ValidationResult(Boolean valid, String errorMessage) {
        this.valid = valid;
        this.errorMessage = errorMessage;
    }

    public boolean isValid() {
        return valid;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
