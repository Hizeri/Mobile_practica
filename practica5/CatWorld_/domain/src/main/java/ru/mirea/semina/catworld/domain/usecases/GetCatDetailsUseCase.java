package ru.mirea.semina.catworld.domain.usecases;

import ru.mirea.semina.catworld.domain.models.CatBreed;
import ru.mirea.semina.catworld.domain.repository.CatRepository;

// Use Case для получения подробной информации
// о выбранной породе кошки.
public class GetCatDetailsUseCase {

    private CatRepository catRepository;

    public GetCatDetailsUseCase(CatRepository catRepository) {
        this.catRepository = catRepository;
    }

    // Получаем id выбранной породы
    // и возвращаем соответствующий объект CatBreed.
    public CatBreed execute(int id) {
        return catRepository.getCatBreedById(id);
    }
}