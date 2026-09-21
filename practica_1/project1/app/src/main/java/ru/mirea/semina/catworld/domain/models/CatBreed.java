package ru.mirea.semina.catworld.domain.models;

// Модель породы кошки.
public class CatBreed {

    private int id;
    private String name;
    private String description;

    // Конструктор.
    public CatBreed(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    // Получить id породы.
    public int getId() {
        return id;
    }

    // Получить название породы.
    public String getName() {
        return name;
    }

    // Получить описание породы.
    public String getDescription() {
        return description;
    }
}