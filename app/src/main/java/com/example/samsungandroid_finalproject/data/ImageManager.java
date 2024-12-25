package com.example.samsungandroid_finalproject.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ImageManager {
    private static final String plantImagesDir = "PlantImagesDir";

    public String generatePlantImageName() {
        LocalDateTime curDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
        return "plantImg_" + curDateTime.format(formatter) + ".png";
    }

    public Uri getPlantImageUri(Context context) throws IOException {
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyGardenApp");
        Path dirPath = Paths.get(dir.getAbsolutePath());

        if (Files.notExists(dirPath)) {
            Files.createDirectory(dirPath);
        }

        String plantImageName = generatePlantImageName();
        File plantImageFile = new File(dir, plantImageName);

        return FileProvider.getUriForFile(
                context,
                "com.example.mygardenapp.provider",
                plantImageFile);
    }

    public File getPlantImagesDir(Context context) throws IOException {
        File dir = new File(context.getFilesDir(), plantImagesDir);
        Path dirPath = Paths.get(dir.getAbsolutePath());

        if (Files.notExists(dirPath)) { // Создаем PlantImagesDir, т.к. её еще не существует
            Files.createDirectory(dirPath);
            return dir;
        }
        return dir;
    }

    // Сохранение изображения растения в локальное хранилище
    public void savePlantImage(Context context, Bitmap plantImage, String plantImageName) throws IOException {
        File dir = getPlantImagesDir(context);
        File plantImageFile = new File(dir, plantImageName);

        try (FileOutputStream fos = new FileOutputStream(plantImageFile)) {
            plantImage.compress(Bitmap.CompressFormat.PNG, 85, fos);
            fos.flush();
        }
    }

    public void renamePlantImage(Context context, String prevImageName, String newImageName) throws IOException {
        File dir = getPlantImagesDir(context);
        File plantImageFile = new File(dir, prevImageName);
        Path plantImagePath = Paths.get(plantImageFile.getAbsolutePath());

        Files.move(plantImagePath, plantImagePath.resolveSibling(newImageName));
    }

    // Удаление изображения растения из локального хранилища
    public void deletePlantImage(Context context, String plantImageName) throws IOException {
        File dir = new File(context.getFilesDir(), plantImagesDir);
        File plantImageFile = new File(dir, plantImageName);
        Path filePathToDelete = Paths.get(plantImageFile.getAbsolutePath());

        Files.deleteIfExists(filePathToDelete);
    }
}
