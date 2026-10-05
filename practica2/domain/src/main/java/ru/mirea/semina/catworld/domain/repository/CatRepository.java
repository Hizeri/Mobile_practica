package ru.mirea.semina.catworld.domain.repository;

import java.util.List;

import ru.mirea.semina.catworld.domain.models.CatBreed;

// Интерфейс репозитория.
// Он описывает операции, которые нужны приложению.
public interface CatRepository {

    // Получить список всех пород.
    List<CatBreed> getCatBreeds();

    // Получить конкретную породу по id.
    CatBreed getCatBreedById(int id);

    // Найти породы по названию.
    List<CatBreed> searchCatBreeds(String query);

    // Добавить породу в избранное.
    boolean saveFavorite(CatBreed breed);

    // Получить список избранных пород.
    List<CatBreed> getFavorites();

    // Удалить породу из избранного.
    boolean deleteFavorite(int id);

    // Тестовое распознавание породы по изображению.
    CatBreed recognizeCatBreed(String imagePath);
}