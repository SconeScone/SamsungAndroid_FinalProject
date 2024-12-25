package com.example.samsungandroid_finalproject.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.samsungandroid_finalproject.domain.Plant;

import java.util.List;

@Dao
public interface PlantDao {
    @Query("SELECT * FROM plants")
    List<Plant> getAllPlants();

    @Query("SELECT * FROM plants WHERE id=:id")
    Plant getPlantById(int id);

    @Query("DELETE FROM plants WHERE id=:id")
    void deletePlantById(int id);

    @Insert
    void insertPlant(Plant plant);

    @Update
    void updatePlant(Plant plant);
}
