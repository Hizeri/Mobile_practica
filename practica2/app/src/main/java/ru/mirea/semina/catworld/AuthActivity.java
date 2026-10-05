package ru.mirea.semina.catworld;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ru.mirea.semina.catworld.data.auth.FirebaseAuthRepository;
import ru.mirea.semina.catworld.domain.auth.AuthRepository;
import ru.mirea.semina.catworld.domain.auth.usecases.IsUserLoggedInUseCase;
import ru.mirea.semina.catworld.domain.auth.usecases.LoginUserUseCase;
import ru.mirea.semina.catworld.domain.auth.usecases.RegisterUserUseCase;

// Экран входа и регистрации пользователя.
public class AuthActivity extends AppCompatActivity {

    // UseCase для входа.
    private LoginUserUseCase loginUserUseCase;

    // UseCase для регистрации.
    private RegisterUserUseCase registerUserUseCase;

    // UseCase для проверки текущей сессии.
    private IsUserLoggedInUseCase isUserLoggedInUseCase;

    // Поле ввода e-mail.
    private EditText editTextEmail;

    // Поле ввода пароля.
    private EditText editTextPassword;

    // Поле для вывода результата.
    private TextView textViewAuthResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Создаём реализацию репозитория через Firebase.
        AuthRepository authRepository =
                new FirebaseAuthRepository();

        // Создаём UseCase для входа.
        loginUserUseCase =
                new LoginUserUseCase(authRepository);

        // Создаём UseCase для регистрации.
        registerUserUseCase =
                new RegisterUserUseCase(authRepository);

        // Создаём UseCase для проверки сессии.
        isUserLoggedInUseCase =
                new IsUserLoggedInUseCase(authRepository);

        // Проверяем, авторизован ли пользователь.
        if (isUserLoggedInUseCase.execute()) {

            // Если сессия уже есть, открываем главный экран.
            openMainActivity();

            return;
        }

        // Подключаем разметку экрана авторизации.
        setContentView(R.layout.activity_auth);

        // Находим поле e-mail.
        editTextEmail =
                findViewById(R.id.editTextEmail);

        // Находим поле пароля.
        editTextPassword =
                findViewById(R.id.editTextPassword);

        // Находим поле сообщений.
        textViewAuthResult =
                findViewById(R.id.textViewAuthResult);

        // Находим кнопку входа.
        Button buttonLogin =
                findViewById(R.id.buttonLogin);

        // Находим кнопку регистрации.
        Button buttonRegister =
                findViewById(R.id.buttonRegister);

        // Обрабатываем вход.
        buttonLogin.setOnClickListener(view ->
                loginUser()
        );

        // Обрабатываем регистрацию.
        buttonRegister.setOnClickListener(view ->
                registerUser()
        );
    }

    // Выполняем вход пользователя.
    private void loginUser() {

        // Получаем e-mail.
        String email =
                editTextEmail
                        .getText()
                        .toString()
                        .trim();

        // Получаем пароль.
        String password =
                editTextPassword
                        .getText()
                        .toString()
                        .trim();

        // Проверяем заполнение полей.
        if (email.isEmpty() || password.isEmpty()) {

            textViewAuthResult.setText(
                    "Введите e-mail и пароль"
            );

            return;
        }

        // Показываем процесс входа.
        textViewAuthResult.setText(
                "Вход..."
        );

        // Выполняем вход через UseCase.
        loginUserUseCase.execute(
                email,
                password,
                new AuthRepository.AuthCallback() {

                    @Override
                    public void onSuccess() {

                        // Вход выполнен успешно.
                        textViewAuthResult.setText(
                                "Вход выполнен успешно"
                        );

                        // Открываем главный экран.
                        openMainActivity();
                    }

                    @Override
                    public void onError(String message) {

                        // Показываем ошибку входа.
                        textViewAuthResult.setText(
                                "Ошибка входа: " + message
                        );
                    }
                }
        );
    }

    // Выполняем регистрацию пользователя.
    private void registerUser() {

        // Получаем e-mail.
        String email =
                editTextEmail
                        .getText()
                        .toString()
                        .trim();

        // Получаем пароль.
        String password =
                editTextPassword
                        .getText()
                        .toString()
                        .trim();

        // Проверяем заполнение полей.
        if (email.isEmpty() || password.isEmpty()) {

            textViewAuthResult.setText(
                    "Введите e-mail и пароль"
            );

            return;
        }

        // Проверяем длину пароля.
        if (password.length() < 6) {

            textViewAuthResult.setText(
                    "Пароль должен содержать минимум 6 символов"
            );

            return;
        }

        // Показываем процесс регистрации.
        textViewAuthResult.setText(
                "Регистрация..."
        );

        // Выполняем регистрацию через UseCase.
        registerUserUseCase.execute(
                email,
                password,
                new AuthRepository.AuthCallback() {

                    @Override
                    public void onSuccess() {

                        // Регистрация выполнена успешно.
                        textViewAuthResult.setText(
                                "Регистрация выполнена успешно"
                        );

                        // Открываем главный экран.
                        openMainActivity();
                    }

                    @Override
                    public void onError(String message) {

                        // Показываем ошибку регистрации.
                        textViewAuthResult.setText(
                                "Ошибка регистрации: " + message
                        );
                    }
                }
        );
    }

    // Открываем главный экран приложения.
    private void openMainActivity() {

        Intent intent =
                new Intent(
                        AuthActivity.this,
                        MainActivity.class
                );

        startActivity(intent);

        // Закрываем экран авторизации.
        finish();
    }
}