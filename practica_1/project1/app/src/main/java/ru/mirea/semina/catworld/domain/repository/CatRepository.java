package ru.mirea.semina.catworld.domain.repository;

import java.util.List;

import ru.mirea.semina.catworld.domain.models.CatBreed;

// Интерфейс репозитория.
// Здесь описываем, какие действия доступны приложению.
public interface CatRepository {

    // Получить все породы.
    List<CatBreed> getCatBreeds();

    // Найти породу по названию.
    List<CatBreed> searchCatBreeds(String query);

    // Получить подробную информацию о породе по id.
    CatBreed getCatDetails(int id);

    // Добавить породу в избранное.
    boolean saveFavorite(CatBreed catBreed);

    // Получить список избранных пород.
    List<CatBreed> getFavorites();

    // Удалить породу из избранного.
    boolean deleteFavorite(int id);

    // Распознать породу по изображению.
    CatBreed recognizeCatBreed(String imageName);
}