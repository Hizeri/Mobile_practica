package ru.mirea.semina.catworld.data.storage;

import java.util.List;

import ru.mirea.semina.catworld.domain.models.CatBreed;

// Интерфейс локального хранилища избранных пород.
public interface CatStorage {

    // Добавить породу в избранное.
    boolean saveFavorite(CatBreed catBreed);

    // Получить все избранные породы.
    List<CatBreed> getFavorites();

    // Удалить породу из избранного.
    boolean deleteFavorite(int id);
}