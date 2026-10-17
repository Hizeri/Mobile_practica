package ru.mirea.semina.catworld.domain.auth.usecases;

import ru.mirea.semina.catworld.domain.auth.AuthRepository;

// UseCase для выхода из аккаунта.
public class LogoutUserUseCase {

    private final AuthRepository repository;

    public LogoutUserUseCase(AuthRepository repository) {
        this.repository = repository;
    }

    // Выполняем выход пользователя.
    public void execute() {
        repository.logout();
    }
}