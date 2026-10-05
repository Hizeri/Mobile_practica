package ru.mirea.semina.catworld.data.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ru.mirea.semina.catworld.data.database.dao.FavoriteCatDao;
import ru.mirea.semina.catworld.data.database.entity.FavoriteCatEntity;

// Главный класс базы данных Room.
@Database(
        entities = {FavoriteCatEntity.class},
        version = 1,
        exportSchema = false
)
public abstract class CatDatabase extends RoomDatabase {

    // Возвращаем DAO для работы с избранным.
    public abstract FavoriteCatDao favoriteCatDao();
}