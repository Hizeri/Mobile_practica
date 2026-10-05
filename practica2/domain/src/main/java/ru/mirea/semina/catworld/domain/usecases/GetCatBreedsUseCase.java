package ru.mirea.semina.catworld.domain.usecases;

import java.util.List;

import ru.mirea.semina.catworld.domain.models.CatBreed;
import ru.mirea.semina.catworld.domain.repository.CatRepository;

// Use Case для получения списка всех пород кошек.
public class GetCatBreedsUseCase {

    // Репозиторий, через который Use Case получает данные.
    private CatRepository catRepository;

    // Репозиторий передаётся в класс через конструктор.
    public GetCatBreedsUseCase(CatRepository catRepository) {
        this.catRepository = catRepository;
    }

    // Выполнение сценария:
    // запросить у репозитория список пород и вернуть его.
    public List<CatBreed> execute() {
        return catRepository.getCatBreeds();
    }
}