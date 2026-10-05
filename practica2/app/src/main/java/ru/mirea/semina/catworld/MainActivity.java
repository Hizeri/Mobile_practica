package ru.mirea.semina.catworld;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import ru.mirea.semina.catworld.data.auth.FirebaseAuthRepository;
import ru.mirea.semina.catworld.data.network.MockNetworkApi;
import ru.mirea.semina.catworld.data.network.NetworkApi;
import ru.mirea.semina.catworld.data.repository.CatRepositoryImpl;
import ru.mirea.semina.catworld.data.storage.CatStorage;
import ru.mirea.semina.catworld.data.storage.RoomCatStorage;
import ru.mirea.semina.catworld.domain.auth.AuthRepository;
import ru.mirea.semina.catworld.domain.auth.usecases.LogoutUserUseCase;
import ru.mirea.semina.catworld.domain.models.CatBreed;
import ru.mirea.semina.catworld.domain.repository.CatRepository;
import ru.mirea.semina.catworld.domain.usecases.DeleteFavoriteUseCase;
import ru.mirea.semina.catworld.domain.usecases.GetCatBreedsUseCase;
import ru.mirea.semina.catworld.domain.usecases.GetCatDetailsUseCase;
import ru.mirea.semina.catworld.domain.usecases.GetFavoritesUseCase;
import ru.mirea.semina.catworld.domain.usecases.RecognizeCatBreedUseCase;
import ru.mirea.semina.catworld.domain.usecases.SaveFavoriteUseCase;
import ru.mirea.semina.catworld.domain.usecases.SearchCatBreedsUseCase;

// Главный экран приложения.
public class MainActivity extends AppCompatActivity {

    // UseCase для получения списка пород.
    private GetCatBreedsUseCase getCatBreedsUseCase;

    // UseCase для поиска.
    private SearchCatBreedsUseCase searchCatBreedsUseCase;

    // UseCase для карточки породы.
    private GetCatDetailsUseCase getCatDetailsUseCase;

    // UseCase для добавления в избранное.
    private SaveFavoriteUseCase saveFavoriteUseCase;

    // UseCase для получения избранного.
    private GetFavoritesUseCase getFavoritesUseCase;

    // UseCase для удаления из избранного.
    private DeleteFavoriteUseCase deleteFavoriteUseCase;

    // UseCase для распознавания.
    private RecognizeCatBreedUseCase recognizeCatBreedUseCase;

    // UseCase для выхода.
    private LogoutUserUseCase logoutUserUseCase;

    // Поле вывода результата.
    private TextView textViewResult;

    // Определяет, является ли пользователь гостем.
    private boolean isGuest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Подключаем разметку.
        setContentView(R.layout.activity_main);

        // Получаем информацию о типе пользователя.
        isGuest =
                getIntent().getBooleanExtra(
                        "IS_GUEST",
                        false
                );

        // Создаём сетевой источник.
        NetworkApi networkApi =
                new MockNetworkApi();

        // Создаём локальное хранилище Room.
        CatStorage catStorage =
                new RoomCatStorage(this);

        // Создаём репозиторий кошек.
        CatRepository catRepository =
                new CatRepositoryImpl(
                        networkApi,
                        catStorage
                );

        // Создаём репозиторий авторизации.
        AuthRepository authRepository =
                new FirebaseAuthRepository();

        // Создаём UseCase выхода.
        logoutUserUseCase =
                new LogoutUserUseCase(
                        authRepository
                );

        // Создаём UseCase получения пород.
        getCatBreedsUseCase =
                new GetCatBreedsUseCase(
                        catRepository
                );

        // Создаём UseCase поиска.
        searchCatBreedsUseCase =
                new SearchCatBreedsUseCase(
                        catRepository
                );

        // Создаём UseCase карточки.
        getCatDetailsUseCase =
                new GetCatDetailsUseCase(
                        catRepository
                );

        // Создаём UseCase добавления.
        saveFavoriteUseCase =
                new SaveFavoriteUseCase(
                        catRepository
                );

        // Создаём UseCase избранного.
        getFavoritesUseCase =
                new GetFavoritesUseCase(
                        catRepository
                );

        // Создаём UseCase удаления.
        deleteFavoriteUseCase =
                new DeleteFavoriteUseCase(
                        catRepository
                );

        // Создаём UseCase распознавания.
        recognizeCatBreedUseCase =
                new RecognizeCatBreedUseCase(
                        catRepository
                );

        // Находим элементы интерфейса.
        EditText editTextSearch =
                findViewById(R.id.editTextSearch);

        textViewResult =
                findViewById(R.id.textViewResult);

        Button buttonGetBreeds =
                findViewById(R.id.buttonGetBreeds);

        Button buttonSearch =
                findViewById(R.id.buttonSearch);

        Button buttonDetails =
                findViewById(R.id.buttonDetails);

        Button buttonAddFavorite =
                findViewById(R.id.buttonAddFavorite);

        Button buttonShowFavorites =
                findViewById(R.id.buttonShowFavorites);

        Button buttonDeleteFavorite =
                findViewById(R.id.buttonDeleteFavorite);

        Button buttonRecognize =
                findViewById(R.id.buttonRecognize);

        Button buttonLogout =
                findViewById(R.id.buttonLogout);

        // Если пользователь гость,
        // запрещаем функции избранного.
        if (isGuest) {

            buttonAddFavorite.setVisibility(
                    View.GONE
            );

            buttonShowFavorites.setVisibility(
                    View.GONE
            );

            buttonDeleteFavorite.setVisibility(
                    View.GONE
            );

            // Для гостя кнопка выхода заменяется
            // на кнопку возврата к авторизации.
            buttonLogout.setText(
                    "Вернуться к авторизации"
            );
        }

        // Показываем список пород.
        buttonGetBreeds.setOnClickListener(view -> {

            List<CatBreed> breeds =
                    getCatBreedsUseCase.execute();

            StringBuilder result =
                    new StringBuilder();

            for (CatBreed breed : breeds) {

                result
                        .append(breed.getName())
                        .append("\n");
            }

            textViewResult.setText(
                    result.toString()
            );
        });

        // Выполняем поиск породы.
        buttonSearch.setOnClickListener(view -> {

            String query =
                    editTextSearch
                            .getText()
                            .toString()
                            .trim();

            if (query.isEmpty()) {

                textViewResult.setText(
                        "Введите название породы"
                );

                return;
            }

            List<CatBreed> breeds =
                    searchCatBreedsUseCase.execute(
                            query
                    );

            if (breeds.isEmpty()) {

                textViewResult.setText(
                        "Порода не найдена"
                );

                return;
            }

            StringBuilder result =
                    new StringBuilder();

            for (CatBreed breed : breeds) {

                result
                        .append(breed.getName())
                        .append("\n");
            }

            textViewResult.setText(
                    result.toString()
            );
        });

        // Показываем карточку Бенгальской кошки.
        buttonDetails.setOnClickListener(view -> {

            CatBreed breed =
                    getCatDetailsUseCase.execute(5);

            if (breed != null) {

                textViewResult.setText(
                        "Порода: "
                                + breed.getName()
                                + "\n\nОписание: "
                                + breed.getDescription()
                );

            } else {

                textViewResult.setText(
                        "Порода не найдена"
                );
            }
        });

        // Добавляем Бенгальскую кошку в избранное.
        buttonAddFavorite.setOnClickListener(view -> {

            CatBreed breed =
                    getCatDetailsUseCase.execute(5);

            if (breed == null) {

                textViewResult.setText(
                        "Бенгальская кошка не найдена"
                );

                return;
            }

            boolean saved =
                    saveFavoriteUseCase.execute(
                            breed
                    );

            if (saved) {

                textViewResult.setText(
                        "Бенгальская кошка добавлена в избранное"
                );

            } else {

                textViewResult.setText(
                        "Эта порода уже находится в избранном"
                );
            }
        });

        // Показываем избранное.
        buttonShowFavorites.setOnClickListener(view -> {

            List<CatBreed> favorites =
                    getFavoritesUseCase.execute();

            if (favorites.isEmpty()) {

                textViewResult.setText(
                        "Избранное пусто"
                );

                return;
            }

            StringBuilder result =
                    new StringBuilder(
                            "Избранное:\n"
                    );

            for (CatBreed breed : favorites) {

                result
                        .append(breed.getName())
                        .append("\n");
            }

            textViewResult.setText(
                    result.toString()
            );
        });

        // Удаляем Бенгальскую кошку из избранного.
        buttonDeleteFavorite.setOnClickListener(view -> {

            boolean deleted =
                    deleteFavoriteUseCase.execute(5);

            if (deleted) {

                textViewResult.setText(
                        "Бенгальская кошка удалена из избранного"
                );

            } else {

                textViewResult.setText(
                        "Бенгальской кошки нет в избранном"
                );
            }
        });

        // Выполняем учебное распознавание.
        buttonRecognize.setOnClickListener(view -> {

            CatBreed recognizedBreed =
                    recognizeCatBreedUseCase.execute(
                            "cat_photo.jpg"
                    );

            if (recognizedBreed != null) {

                textViewResult.setText(
                        "Распознанная порода: "
                                + recognizedBreed.getName()
                );

            } else {

                textViewResult.setText(
                        "Не удалось распознать породу"
                );
            }
        });

        // Выход из аккаунта.
        buttonLogout.setOnClickListener(view -> {

            if (isGuest) {

                // Гость просто возвращается
                // на экран авторизации.
                Intent intent =
                        new Intent(
                                MainActivity.this,
                                AuthActivity.class
                        );

                startActivity(intent);

                finish();

            } else {

                // Авторизованный пользователь
                // выходит через UseCase.
                logoutUserUseCase.execute();

                Intent intent =
                        new Intent(
                                MainActivity.this,
                                AuthActivity.class
                        );

                startActivity(intent);

                finish();
            }
        });
    }
}