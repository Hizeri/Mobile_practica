package ru.mirea.semina.catworld.domain.usecases;

import ru.mirea.semina.catworld.domain.models.CatBreed;
import ru.mirea.semina.catworld.domain.repository.CatRepository;

public class GetCatDetailsUseCase {

    private final CatRepository repository;

    public GetCatDetailsUseCase(CatRepository repository) {
        this.repository = repository;
    }

    public CatBreed execute(int id) {
        return repository.getCatDetails(id);
    }
}