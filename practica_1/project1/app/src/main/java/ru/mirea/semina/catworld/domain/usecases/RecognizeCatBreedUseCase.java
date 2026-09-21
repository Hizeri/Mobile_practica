package ru.mirea.semina.catworld.domain.usecases;

import ru.mirea.semina.catworld.domain.models.CatBreed;
import ru.mirea.semina.catworld.domain.repository.CatRepository;

public class RecognizeCatBreedUseCase {

    private final CatRepository repository;

    public RecognizeCatBreedUseCase(CatRepository repository) {
        this.repository = repository;
    }

    public CatBreed execute(String imageName) {
        return repository.recognizeCatBreed(imageName);
    }
}