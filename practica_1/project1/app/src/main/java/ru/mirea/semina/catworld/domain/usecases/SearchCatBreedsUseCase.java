package ru.mirea.semina.catworld.domain.usecases;

import java.util.List;

import ru.mirea.semina.catworld.domain.models.CatBreed;
import ru.mirea.semina.catworld.domain.repository.CatRepository;

public class SearchCatBreedsUseCase {

    private final CatRepository repository;

    public SearchCatBreedsUseCase(CatRepository repository) {
        this.repository = repository;
    }

    public List<CatBreed> execute(String query) {
        return repository.searchCatBreeds(query);
    }
}