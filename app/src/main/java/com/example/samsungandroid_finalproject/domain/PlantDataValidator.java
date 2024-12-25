package com.example.samsungandroid_finalproject.domain;

public class PlantDataValidator {
    public static ValidationResult validatePlantName(String plantName) {
        if (plantName.matches("^\\s*$")) {
            return new ValidationResult(
                    false,
                    "Введите наименование растения"
            );
        }

        if (!plantName.matches("^[а-яА-ЯёЁa-zA-Z ]+$")) {
            return new ValidationResult(
                    false,
                    "Наименование растения может содержать только буквы и пробелы"
            );
        }

        return new ValidationResult(
                true,
                null
        );
    }

    public static ValidationResult validatePlantVariety(String plantVariety) {
        if (plantVariety.matches("^\\s*$")) {
            return new ValidationResult(
                    false,
                    "Введите сорт растения"
            );
        }

        if (!plantVariety.matches("^[а-яА-ЯёЁa-zA-Z0-9 ]+$")) {
            return new ValidationResult(
                    false,
                    "Сорт растения может содержать только буквы, цифры и пробелы"
            );
        }

        return new ValidationResult(
                true,
                null
        );

    }
}
