package ru.mirea.semina.catworld.domain.usecases;

import ru.mirea.semina.catworld.domain.models.CatBreed;
import ru.mirea.semina.catworld.domain.repository.CatRepository;

// Use Case для распознавания породы кошки по изображению.
public class RecognizeCatBreedUseCase {

    private CatRepository catRepository;

    public RecognizeCatBreedUseCase(CatRepository catRepository) {
        this.catRepository = catRepository;
    }

    // Пока imagePath используется только как параметр.
    // Настоящую TensorFlow Lite модель подключим в дальнейших практиках.
    public CatBreed execute(String imagePath) {
        return catRepository.recognizeCatBreed(imagePath);
    }
}