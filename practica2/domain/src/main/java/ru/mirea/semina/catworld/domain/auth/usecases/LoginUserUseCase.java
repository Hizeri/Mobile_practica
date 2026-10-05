package ru.mirea.semina.catworld.domain.auth.usecases;

import ru.mirea.semina.catworld.domain.auth.AuthRepository;

// UseCase для входа пользователя.
public class LoginUserUseCase {

    private final AuthRepository repository;

    public LoginUserUseCase(AuthRepository repository) {
        this.repository = repository;
    }

    // Передаём данные пользователя в репозиторий.
    public void execute(
            String email,
            String password,
            AuthRepository.AuthCallback callback
    ) {
        repository.login(
                email,
                password,
                callback
        );
    }
}