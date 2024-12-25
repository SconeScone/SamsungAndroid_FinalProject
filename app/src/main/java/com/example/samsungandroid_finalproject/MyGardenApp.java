package com.example.samsungandroid_finalproject;

import android.app.Application;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyGardenApp extends Application {
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public Executor getExecutor() {
        return executorService;
    }
}
