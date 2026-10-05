package ru.mirea.semina.catworld.domain.auth.usecases;

import ru.mirea.semina.catworld.domain.auth.AuthRepository;

// UseCase для регистрации пользователя.
public class RegisterUserUseCase {

    private final AuthRepository repository;

    public RegisterUserUseCase(AuthRepository repository) {
        this.repository = repository;
    }

    // Передаём данные нового пользователя в репозиторий.
    public void execute(
            String email,
            String password,
            AuthRepository.AuthCallback callback
    ) {
        repository.register(
                email,
                password,
                callback
        );
    }
}