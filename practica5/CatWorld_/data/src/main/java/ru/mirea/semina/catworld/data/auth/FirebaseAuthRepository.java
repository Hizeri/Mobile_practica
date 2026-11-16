package ru.mirea.semina.catworld.data.auth;

import com.google.firebase.auth.FirebaseAuth;

import ru.mirea.semina.catworld.domain.auth.AuthRepository;

// Реализация авторизации через Firebase.
public class FirebaseAuthRepository implements AuthRepository {

    // Объект Firebase Authentication.
    private final FirebaseAuth firebaseAuth;

    public FirebaseAuthRepository() {

        // Получаем экземпляр Firebase Auth.
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void login(
            String email,
            String password,
            AuthCallback callback
    ) {

        // Выполняем вход через Firebase.
        firebaseAuth
                .signInWithEmailAndPassword(
                        email,
                        password
                )
                .addOnCompleteListener(task -> {

                    // Если Firebase успешно выполнил вход.
                    if (task.isSuccessful()) {

                        callback.onSuccess();

                    } else {

                        // Получаем текст ошибки Firebase.
                        String message =
                                "Ошибка входа";

                        if (task.getException() != null) {
                            message =
                                    task.getException()
                                            .getMessage();
                        }

                        callback.onError(message);
                    }
                });
    }

    @Override
    public void register(
            String email,
            String password,
            AuthCallback callback
    ) {

        // Создаём нового пользователя в Firebase.
        firebaseAuth
                .createUserWithEmailAndPassword(
                        email,
                        password
                )
                .addOnCompleteListener(task -> {

                    // Если регистрация успешна.
                    if (task.isSuccessful()) {

                        callback.onSuccess();

                    } else {

                        // Получаем текст ошибки Firebase.
                        String message =
                                "Ошибка регистрации";

                        if (task.getException() != null) {
                            message =
                                    task.getException()
                                            .getMessage();
                        }

                        callback.onError(message);
                    }
                });
    }

    @Override
    public void logout() {

        // Завершаем текущую Firebase-сессию.
        firebaseAuth.signOut();
    }

    @Override
    public boolean isUserLoggedIn() {

        // Если currentUser не null, пользователь уже вошёл.
        return firebaseAuth.getCurrentUser() != null;
    }
}