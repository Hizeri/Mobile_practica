package ru.mirea.semina.catworld.domain.usecases;

import java.util.List;

import ru.mirea.semina.catworld.domain.models.CatBreed;
import ru.mirea.semina.catworld.domain.repository.CatRepository;

// Use Case для поиска пород кошек по названию.
public class SearchCatBreedsUseCase {

    private CatRepository catRepository;

    public SearchCatBreedsUseCase(CatRepository catRepository) {
        this.catRepository = catRepository;
    }

    // query — текст, введённый пользователем в строку поиска.
    public List<CatBreed> execute(String query) {
        return catRepository.searchCatBreeds(query);
    }
}
