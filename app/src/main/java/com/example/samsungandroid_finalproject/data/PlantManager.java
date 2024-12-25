package com.example.samsungandroid_finalproject.data;

import android.content.Context;

import com.example.samsungandroid_finalproject.domain.Plant;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;

public class PlantManager {
    private final ImageManager imageManager;
    private final PlantDao plantDao;
    private final Executor executor;
    private final Context context;

    public PlantManager(PlantDao plantDao, Executor executor, Context context) {
        imageManager = new ImageManager();
        this.plantDao = plantDao;
        this.executor = executor;
        this.context = context;
    }

    public void getAllPlants(ResultCallback<List<Plant>> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<Plant> plants = plantDao.getAllPlants();
                PlantOperationResult<List<Plant>> result = new PlantOperationResult<>(
                        plants,
                        true,
                        ""
                );
                callback.onComplete(result);
            }
        });
    }

    public void getPlantById(int id, ResultCallback<Plant> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Plant plant = plantDao.getPlantById(id);
                PlantOperationResult<Plant> result = new PlantOperationResult<>(
                        plant,
                        true,
                        ""
                );
                callback.onComplete(result);
            }
        });
    }

    public void insertPlant(Plant plant, ResultCallback<Void> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (plant.getImage() != null) {
                        String plantImageName = imageManager.generatePlantImageName();
                        imageManager.savePlantImage(context, plant.getImage(), plantImageName);
                        plant.setImagePath(plantImageName);
                    }

                    plantDao.insertPlant(plant);
                    callback.onComplete(new PlantOperationResult<>(
                            null,
                            true,
                            ""
                    ));
                } catch (IOException e) {
                    callback.onComplete(new PlantOperationResult<>(
                            null,
                            false,
                            "Что-то пошло не так при добавлении растения"
                    ));
                }
            }
        });
    }

    public void updatePlant(Plant plant, ResultCallback<Void> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (plant.getImagePath() == null && plant.getImage() != null) {
                        String plantImageName = imageManager.generatePlantImageName();
                        imageManager.savePlantImage(context, plant.getImage(), plantImageName);
                        plant.setImagePath(plantImageName);
                    } else if (plant.getImagePath() != null && plant.getImage() != null) { // Обновляем существующее изображение
                        String newImageName = imageManager.generatePlantImageName();
                        imageManager.renamePlantImage(context, plant.getImagePath(), newImageName);
                        plant.setImagePath(newImageName);
                        imageManager.savePlantImage(context, plant.getImage(), plant.getImagePath());
                    }

                    plantDao.updatePlant(plant);
                    callback.onComplete(new PlantOperationResult<>(
                            null,
                            true,
                            ""
                    ));
                } catch (IOException e) {
                    callback.onComplete(new PlantOperationResult<>(
                            null,
                            false,
                            "Что-то пошло не так при обновлении информации о растении"
                    ));
                }
            }
        });
    }

    public void deletePlant(Plant plant, ResultCallback<Void> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (plant.getImagePath() != null) {
                        imageManager.deletePlantImage(context, plant.getImagePath());
                    }
                    plantDao.deletePlantById(plant.getId());
                    callback.onComplete(new PlantOperationResult<>(
                            null,
                            true,
                            ""
                    ));
                } catch (IOException e) {
                    callback.onComplete(new PlantOperationResult<>(
                            null,
                            false,
                            "Что-то пошло не так при удалении растения"
                    ));
                }
            }
        });
    }
}
