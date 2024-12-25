package com.example.samsungandroid_finalproject.ui;

public class FragmentAction {
    private final boolean close;
    private final String errorMessage;

    public FragmentAction(boolean close, String errorMessage) {
        this.close = close;
        this.errorMessage = errorMessage;
    }

    public boolean canClose() {
        return close;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
