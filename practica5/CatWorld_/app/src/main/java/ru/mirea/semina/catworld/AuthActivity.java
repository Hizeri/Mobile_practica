
package ru.mirea.semina.catworld;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ru.mirea.semina.catworld.data.auth.FirebaseAuthRepository;
import ru.mirea.semina.catworld.data.storage.SharedPrefClientStorage;
import ru.mirea.semina.catworld.domain.auth.AuthRepository;
import ru.mirea.semina.catworld.domain.auth.usecases.IsUserLoggedInUseCase;
import ru.mirea.semina.catworld.domain.auth.usecases.LoginUserUseCase;
import ru.mirea.semina.catworld.domain.auth.usecases.RegisterUserUseCase;

// Экран входа, регистрации и гостевого доступа.
public class AuthActivity extends AppCompatActivity {

    // UseCase для входа.
    private LoginUserUseCase loginUserUseCase;

    // UseCase для регистрации.
    private RegisterUserUseCase registerUserUseCase;

    // UseCase для проверки текущей сессии.
    private IsUserLoggedInUseCase isUserLoggedInUseCase;

    // Локальное хранилище информации о клиенте.
    private SharedPrefClientStorage clientStorage;

    // Поле ввода электронной почты.
    private EditText editTextEmail;

    // Поле ввода пароля.
    private EditText editTextPassword;

    // Поле для сообщений пользователю.
    private TextView textViewAuthResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Создаём локальное хранилище.
        clientStorage = new SharedPrefClientStorage(this);

        // Создаём репозиторий авторизации Firebase.
        AuthRepository authRepository =
                new FirebaseAuthRepository();

        // Создаём UseCase для входа.
        loginUserUseCase =
                new LoginUserUseCase(authRepository);

        // Создаём UseCase для регистрации.
        registerUserUseCase =
                new RegisterUserUseCase(authRepository);

        // Создаём UseCase для проверки авторизации.
        isUserLoggedInUseCase =
                new IsUserLoggedInUseCase(authRepository);

        // Если пользователь уже авторизован,
        // сразу открываем главный экран.
        if (isUserLoggedInUseCase.execute()) {
            openMainActivity();
            return;
        }

        // Подключаем разметку авторизации.
        setContentView(R.layout.activity_auth);

        // Находим поле электронной почты.
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

        // Находим кнопку гостевого доступа.
        Button buttonGuest =
                findViewById(R.id.buttonGuest);

        // При нажатии выполняем вход.
        buttonLogin.setOnClickListener(view ->
                loginUser()
        );

        // При нажатии выполняем регистрацию.
        buttonRegister.setOnClickListener(view ->
                registerUser()
        );

        // При нажатии открываем приложение как гость.
        buttonGuest.setOnClickListener(view ->
                openGuestActivity()
        );
    }

    // Выполняем вход пользователя через Firebase.
    private void loginUser() {

        // Получаем электронную почту.
        String email = editTextEmail
                .getText()
                .toString()
                .trim();

        // Получаем пароль.
        String password = editTextPassword
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
        textViewAuthResult.setText("Вход...");

        // Отправляем запрос на авторизацию.
        loginUserUseCase.execute(
                email,
                password,
                new AuthRepository.AuthCallback() {

                    @Override
                    public void onSuccess() {

                        // Сохраняем e-mail клиента.
                        clientStorage.saveEmail(email);

                        // Показываем успешный результат.
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

        // Получаем электронную почту.
        String email = editTextEmail
                .getText()
                .toString()
                .trim();

        // Получаем пароль.
        String password = editTextPassword
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
        textViewAuthResult.setText("Регистрация...");

        // Отправляем запрос на регистрацию.
        registerUserUseCase.execute(
                email,
                password,
                new AuthRepository.AuthCallback() {

                    @Override
                    public void onSuccess() {

                        // Сохраняем e-mail клиента.
                        clientStorage.saveEmail(email);

                        // Показываем успешный результат.
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

    // Открываем приложение после входа или регистрации.
    private void openMainActivity() {

        Intent intent = new Intent(
                AuthActivity.this,
                MainActivity.class
        );

        // Пользователь прошёл авторизацию.
        intent.putExtra("IS_GUEST", false);

        startActivity(intent);

        // Закрываем экран авторизации.
        finish();
    }

    // Открываем приложение без авторизации.
    private void openGuestActivity() {

        Intent intent = new Intent(
                AuthActivity.this,
                MainActivity.class
        );

        // Сообщаем главному экрану о гостевом режиме.
        intent.putExtra("IS_GUEST", true);

        // Открываем главный экран.
        startActivity(intent);

        // Закрываем экран авторизации.
        finish();
    }
}
