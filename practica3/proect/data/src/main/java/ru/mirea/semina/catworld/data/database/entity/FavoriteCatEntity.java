package ru.mirea.semina.catworld.data.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Таблица избранных пород.
@Entity(tableName = "favorite_cats")
public class FavoriteCatEntity {

    // Уникальный id породы.
    @PrimaryKey
    private int id;

    private String name;
    private String description;

    // Создаём запись для базы данных.
    public FavoriteCatEntity(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    // Получаем id.
    public int getId() {
        return id;
    }

    // Получаем название.
    public String getName() {
        return name;
    }

    // Получаем описание.
    public String getDescription() {
        return description;
    }
}