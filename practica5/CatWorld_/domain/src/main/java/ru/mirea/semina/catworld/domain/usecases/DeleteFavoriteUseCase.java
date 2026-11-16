package ru.mirea.semina.catworld.domain.usecases;

import ru.mirea.semina.catworld.domain.repository.CatRepository;

// Use Case для удаления породы из избранного.
public class DeleteFavoriteUseCase {

    private CatRepository catRepository;

    public DeleteFavoriteUseCase(CatRepository catRepository) {
        this.catRepository = catRepository;
    }

    // id — идентификатор породы, которую нужно удалить.
    public boolean execute(int id) {
        return catRepository.deleteFavorite(id);
    }
}