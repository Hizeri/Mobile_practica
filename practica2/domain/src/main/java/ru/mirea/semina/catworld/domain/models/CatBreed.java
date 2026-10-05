package ru.mirea.semina.catworld.domain.models;

// Модель породы кошки.
public class CatBreed {

    private int id;
    private String name;
    private String description;

    // Создаём объект породы.
    public CatBreed(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    // Получаем id породы.
    public int getId() {
        return id;
    }

    // Получаем название породы.
    public String getName() {
        return name;
    }

    // Получаем описание породы.
    public String getDescription() {
        return description;
    }
}