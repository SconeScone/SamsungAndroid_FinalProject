package com.example.samsungandroid_finalproject.ui.plant;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.samsungandroid_finalproject.data.PlantManager;
import com.example.samsungandroid_finalproject.data.PlantOperationResult;
import com.example.samsungandroid_finalproject.data.ResultCallback;
import com.example.samsungandroid_finalproject.domain.Plant;
import com.example.samsungandroid_finalproject.ui.FragmentAction;

public class PlantViewModel extends ViewModel {

    private final int plantId;

    private final PlantManager plantManager;

    private MutableLiveData<Plant> plant;

    private final MutableLiveData<FragmentAction> fragmentAction = new MutableLiveData<>(null);

    public LiveData<FragmentAction> getFragmentAction() {
        return fragmentAction;
    }

    public PlantViewModel(int plantId, PlantManager plantManager) {
        this.plantId = plantId;
        this.plantManager = plantManager;
    }

    public LiveData<Plant> getPlant() {
        if (plant == null) {
            plant = new MutableLiveData<>(null);

            plantManager.getPlantById(plantId, new ResultCallback<Plant>() {
                @Override
                public void onComplete(PlantOperationResult<Plant> result) {
                    plant.postValue(result.getData());
                }
            });
        }
        return plant;
    }

    public void deletePlant() {
        plantManager.deletePlant(plant.getValue(), new ResultCallback<Void>() {
            @Override
            public void onComplete(PlantOperationResult<Void> result) {
                if (result.isSuccess()) {
                    fragmentAction.postValue(new FragmentAction(true, ""));
                } else {
                    fragmentAction.postValue(new FragmentAction(false, result.getErrorMessage()));
                }
            }
        });
    }
}
