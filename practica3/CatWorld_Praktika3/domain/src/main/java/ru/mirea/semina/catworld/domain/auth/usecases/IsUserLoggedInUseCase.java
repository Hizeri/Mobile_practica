package ru.mirea.semina.catworld.domain.auth.usecases;

import ru.mirea.semina.catworld.domain.auth.AuthRepository;

// UseCase для проверки текущей авторизации.
public class IsUserLoggedInUseCase {

    private final AuthRepository repository;

    public IsUserLoggedInUseCase(AuthRepository repository) {
        this.repository = repository;
    }

    // Проверяем, сохранена ли активная сессия.
    public boolean execute() {
        return repository.isUserLoggedIn();
    }
}