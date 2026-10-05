package ru.mirea.semina.catworld.data.storage.models;

// Модель породы кошки для слоя data/storage.
// Она не зависит от domain-слоя.
public class CatBreed {

    // Идентификатор породы.
    private int id;

    // Название породы.
    private String name;

    // Дата сохранения данных.
    private String localDate;

    public CatBreed(int id, String name, String localDate) {
        this.id = id;
        this.name = name;
        this.localDate = localDate;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocalDate() {
        return localDate;
    }
}