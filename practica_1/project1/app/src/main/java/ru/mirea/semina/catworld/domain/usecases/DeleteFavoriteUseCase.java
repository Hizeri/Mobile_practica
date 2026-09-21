package ru.mirea.semina.catworld.domain.usecases;

import ru.mirea.semina.catworld.domain.repository.CatRepository;

public class DeleteFavoriteUseCase {

    private final CatRepository repository;

    public DeleteFavoriteUseCase(CatRepository repository) {
        this.repository = repository;
    }

    public boolean execute(int id) {
        return repository.deleteFavorite(id);
    }
}