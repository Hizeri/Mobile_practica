package ru.mirea.semina.catworld.domain.usecases;

import ru.mirea.semina.catworld.domain.models.CatBreed;
import ru.mirea.semina.catworld.domain.repository.CatRepository;

// Use Case для добавления породы кошки в избранное.
public class SaveFavoriteUseCase {

    private CatRepository catRepository;

    public SaveFavoriteUseCase(CatRepository catRepository) {
        this.catRepository = catRepository;
    }

    // Передаём выбранную породу репозиторию на сохранение.
    public boolean execute(CatBreed breed) {
        return catRepository.saveFavorite(breed);
    }
}