package ru.mirea.semina.catworld.data.network;

import java.util.List;

import ru.mirea.semina.catworld.domain.models.CatBreed;

// Интерфейс для получения данных из сети.
public interface NetworkApi {

    // Получаем список пород кошек.
    List<CatBreed> getCatBreeds();

    // Получаем одну породу по id.
    CatBreed getCatBreedById(int id);
}