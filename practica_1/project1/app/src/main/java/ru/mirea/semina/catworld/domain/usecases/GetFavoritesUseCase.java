package ru.mirea.semina.catworld.domain.usecases;

import java.util.List;

import ru.mirea.semina.catworld.domain.models.CatBreed;
import ru.mirea.semina.catworld.domain.repository.CatRepository;

public class GetFavoritesUseCase {

    private final CatRepository repository;

    public GetFavoritesUseCase(CatRepository repository) {
        this.repository = repository;
    }

    public List<CatBreed> execute() {
        return repository.getFavorites();
    }
}