package com.example.samsungandroid_finalproject.domain;

import androidx.room.TypeConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateConverter {
    @TypeConverter
    public static long toLong(LocalDate date) {
        if (date != null) {
            return date.toEpochDay();
        }
        return -1;
    }

    @TypeConverter
    public static LocalDate toDate(long dateLong) {
        if (dateLong != -1) {
            return LocalDate.ofEpochDay(dateLong);
        }
        return null;
    }

    public static LocalDate toDate(String dateStr) {
        if (dateStr.isEmpty()) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return LocalDate.parse(dateStr, formatter);
    }

    public static String toString(LocalDate date) {
        if (date == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return date.format(formatter);
    }

    public static String toString(int day, int month, int year) {
        return (day <= 9 ? "0" + day : day) + "." +
                (month <= 9 ? "0" + month : month) + "." + year;
    }
}
