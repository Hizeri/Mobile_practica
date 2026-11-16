package ru.mirea.semina.catworld.data.storage;

import java.util.List;

import ru.mirea.semina.catworld.domain.models.CatBreed;

// Интерфейс локального хранилища.
public interface CatStorage {

    // Добавляем породу в избранное.
    boolean saveFavorite(CatBreed catBreed);

    // Получаем избранные породы.
    List<CatBreed> getFavorites();

    // Удаляем породу по id.
    boolean deleteFavorite(int id);
}