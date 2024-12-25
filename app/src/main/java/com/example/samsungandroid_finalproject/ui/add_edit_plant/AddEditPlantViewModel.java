package com.example.samsungandroid_finalproject.ui.add_edit_plant;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.samsungandroid_finalproject.data.PlantManager;
import com.example.samsungandroid_finalproject.data.PlantOperationResult;
import com.example.samsungandroid_finalproject.data.ResultCallback;
import com.example.samsungandroid_finalproject.domain.Plant;
import com.example.samsungandroid_finalproject.ui.FragmentAction;

public class AddEditPlantViewModel extends ViewModel {
    private final int plantId;
    private final PlantManager plantManager;
    private MutableLiveData<Plant> plant;
    private final MutableLiveData<Bitmap> plantImage = new MutableLiveData<>(null);
    private final MutableLiveData<FragmentAction> fragmentAction = new MutableLiveData<>(null);

    public AddEditPlantViewModel(int plantId, PlantManager plantManager) {
        this.plantId = plantId;
        this.plantManager = plantManager;
    }

    public void setPlantImage(Bitmap bitmap) {
        plantImage.setValue(bitmap);
    }


    public LiveData<FragmentAction> getFragmentAction() {
        return fragmentAction;
    }


    public LiveData<Plant> getPlant() {
        if (plant == null) {
            plant = new MutableLiveData<>(null);

            if (plantId != 0) {
                plantManager.getPlantById(plantId, new ResultCallback<Plant>() {
                    @Override
                    public void onComplete(PlantOperationResult<Plant> result) {
                        plant.postValue(result.getData());
                    }
                });
            }
        }
        return plant;
    }


    public void savePlant(Plant plantToSave) {
        plantToSave.setId(plantId);
        plantToSave.setImage(plantImage.getValue());
        plantToSave.setImagePath(plant.getValue() != null ? plant.getValue().getImagePath() : null);

        if (plantId == 0) { // Добавляем новое растение
            addPlant(plantToSave);
        } else { // Обновляем данные о растении
            updatePlant(plantToSave);
        }
    }

    private void addPlant(Plant plantToAdd) {
        plantManager.insertPlant(plantToAdd, new ResultCallback<Void>() {
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

    private void updatePlant(Plant plantToUpdate) {
        plantManager.updatePlant(plantToUpdate, new ResultCallback<Void>() {
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
