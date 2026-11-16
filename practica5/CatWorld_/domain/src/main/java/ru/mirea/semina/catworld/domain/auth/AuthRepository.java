package ru.mirea.semina.catworld.domain.auth;

// Интерфейс для работы с авторизацией пользователя.
public interface AuthRepository {

    // Выполняем вход пользователя.
    void login(
            String email,
            String password,
            AuthCallback callback
    );

    // Регистрируем нового пользователя.
    void register(
            String email,
            String password,
            AuthCallback callback
    );

    // Выполняем выход из аккаунта.
    void logout();

    // Проверяем, есть ли уже авторизованный пользователь.
    boolean isUserLoggedIn();

    // Callback возвращает результат асинхронной операции.
    interface AuthCallback {

        // Вызывается при успешной операции.
        void onSuccess();

        // Вызывается при ошибке.
        void onError(String message);
    }
}