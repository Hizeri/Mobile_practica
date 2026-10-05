package ru.mirea.semina.catworld.data.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ru.mirea.semina.catworld.data.database.entity.FavoriteCatEntity;

// DAO для работы с таблицей избранного.
@Dao
public interface FavoriteCatDao {

    // Получаем все избранные породы.
    @Query("SELECT * FROM favorite_cats")
    List<FavoriteCatEntity> getAll();

    // Добавляем запись в базу.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FavoriteCatEntity cat);

    // Удаляем запись по id.
    @Query("DELETE FROM favorite_cats WHERE id = :id")
    void deleteById(int id);

    // Проверяем, есть ли порода в избранном.
    @Query("SELECT COUNT(*) FROM favorite_cats WHERE id = :id")
    int countById(int id);
}