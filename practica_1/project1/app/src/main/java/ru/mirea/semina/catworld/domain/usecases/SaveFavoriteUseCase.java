package ru.mirea.semina.catworld.domain.usecases;

import ru.mirea.semina.catworld.domain.models.CatBreed;
import ru.mirea.semina.catworld.domain.repository.CatRepository;

public class SaveFavoriteUseCase {

    private final CatRepository repository;

    public SaveFavoriteUseCase(CatRepository repository) {
        this.repository = repository;
    }

    public boolean execute(CatBreed catBreed) {
        return repository.saveFavorite(catBreed);
    }
}