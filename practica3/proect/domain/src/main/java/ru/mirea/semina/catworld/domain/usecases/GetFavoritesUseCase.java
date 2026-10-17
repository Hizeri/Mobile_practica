package ru.mirea.semina.catworld.domain.usecases;

import java.util.List;

import ru.mirea.semina.catworld.domain.models.CatBreed;
import ru.mirea.semina.catworld.domain.repository.CatRepository;

// Use Case для получения избранных пород.
public class GetFavoritesUseCase {

    private CatRepository catRepository;

    public GetFavoritesUseCase(CatRepository catRepository) {
        this.catRepository = catRepository;
    }

    public List<CatBreed> execute() {
        return catRepository.getFavorites();
    }
}