package com.example.samsungandroid_finalproject.ui.plants;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.samsungandroid_finalproject.data.PlantManager;
import com.example.samsungandroid_finalproject.data.PlantOperationResult;
import com.example.samsungandroid_finalproject.data.ResultCallback;
import com.example.samsungandroid_finalproject.domain.Plant;

import java.util.ArrayList;
import java.util.List;

public class PlantsViewModel extends ViewModel {
    private final PlantManager plantManager;
    private MutableLiveData<List<Plant>> plants;

    public PlantsViewModel(PlantManager plantManager) {
        this.plantManager = plantManager;
    }

    public LiveData<List<Plant>> getPlants() {
        if (plants == null) {
            plants = new MutableLiveData<>(new ArrayList<Plant>());

            plantManager.getAllPlants(new ResultCallback<List<Plant>>() {
                @Override
                public void onComplete(PlantOperationResult<List<Plant>> result) {
                    plants.postValue(result.getData());
                }
            });
        }
        return plants;
    }
}
