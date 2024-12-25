package com.example.samsungandroid_finalproject.domain;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

@Entity(tableName = "plants")
public class Plant {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name; // Наименование растения
    private String variety; // Сорт растения
    private LocalDate sowingDate; // Дата посева на рассаду (может отсутствовать)
    private LocalDate plantingDate; // Дата высадки в грунт
    private LocalDate harvestingDate; // Дата сбора урожая
    @Ignore
    private Bitmap image; // Фото растения
    private String imagePath; // Путь к фото растения
    private String description; // Описание растения
    private String growingConditions; // Условия выращивания

    public Plant(int id, String name, String variety, LocalDate sowingDate, LocalDate plantingDate,  LocalDate harvestingDate, String imagePath, String description, String growingConditions) {
        this.id = id;
        this.name = name;
        this.variety = variety;
        this.sowingDate = sowingDate;
        this.plantingDate = plantingDate;
        this.harvestingDate = harvestingDate;
        this.imagePath = imagePath;
        this.description = description;
        this.growingConditions = growingConditions;
    }

    @Ignore
    public Plant(String name, String variety, LocalDate sowingDate, LocalDate plantingDate, LocalDate harvestingDate, String description, String growingConditions) {
        this.name = name;
        this.variety = variety;
        this.sowingDate = sowingDate;
        this.plantingDate = plantingDate;
        this.harvestingDate = harvestingDate;
        this.description = description;
        this.growingConditions = growingConditions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    public String getName() {
        return name;
    }

    public String getVariety() {
        return variety;
    }

    public LocalDate getSowingDate() {
        return sowingDate;
    }

    public LocalDate getPlantingDate() {
        return plantingDate;
    }

    public LocalDate getHarvestingDate() {return harvestingDate;}

    public Bitmap getImage() { return image; }

    public void setImage(Bitmap image) { this.image = image; }

    public String getImagePath() { return imagePath; }

    public void setImagePath(String imagePath) { this.imagePath = imagePath; }


    public String getDescription() { return description; }

    public String getGrowingConditions() { return growingConditions; }
}
