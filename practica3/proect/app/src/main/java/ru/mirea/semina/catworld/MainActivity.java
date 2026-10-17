
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

public class MainActivity extends AppCompatActivity {

    // Сценарии работы с породами кошек
    private GetCatBreedsUseCase getCatBreedsUseCase;
    private SearchCatBreedsUseCase searchCatBreedsUseCase;
    private GetCatDetailsUseCase getCatDetailsUseCase;

    // Сценарии работы с избранным
    private SaveFavoriteUseCase saveFavoriteUseCase;
    private GetFavoritesUseCase getFavoritesUseCase;
    private DeleteFavoriteUseCase deleteFavoriteUseCase;

    // Распознавание и авторизация
    private RecognizeCatBreedUseCase recognizeCatBreedUseCase;
    private LogoutUserUseCase logoutUserUseCase;

    // Поле для отображения результатов
    private TextView textViewResult;

    // Проверка гостевого режима
    private boolean isGuest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Загружаем главный экран CatWorld
        setContentView(R.layout.activity_main);

        // Получаем режим входа пользователя
        isGuest = getIntent().getBooleanExtra(
                "IS_GUEST", false
        );

        // Создаём сетевой источник данных
        NetworkApi networkApi = new MockNetworkApi();

        // Создаём локальное хранилище Room
        CatStorage catStorage = new RoomCatStorage(this);

        // Подключаем репозиторий кошек
        CatRepository catRepository = new CatRepositoryImpl(
                networkApi,
                catStorage
        );

        // Подключаем Firebase авторизацию
        AuthRepository authRepository =
                new FirebaseAuthRepository();

        // Создаём UseCase авторизации
        logoutUserUseCase = new LogoutUserUseCase(
                authRepository
        );

        // Получение всех пород
        getCatBreedsUseCase = new GetCatBreedsUseCase(
                catRepository
        );

        // Поиск породы
        searchCatBreedsUseCase = new SearchCatBreedsUseCase(
                catRepository
        );

        // Получение карточки породы
        getCatDetailsUseCase = new GetCatDetailsUseCase(
                catRepository
        );

        // Добавление в избранное
        saveFavoriteUseCase = new SaveFavoriteUseCase(
                catRepository
        );

        // Получение избранного
        getFavoritesUseCase = new GetFavoritesUseCase(
                catRepository
        );

        // Удаление из избранного
        deleteFavoriteUseCase = new DeleteFavoriteUseCase(
                catRepository
        );

        // Учебное распознавание по фотографии
        recognizeCatBreedUseCase =
                new RecognizeCatBreedUseCase(catRepository);

        // Находим поле поиска
        EditText editTextSearch =
                findViewById(R.id.editTextSearch);

        // Находим поле для вывода информации
        textViewResult =
                findViewById(R.id.textViewResult);

        // Находим кнопки приложения
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

        // Новая кнопка для практической №3
        Button buttonOpenDrawer =
                findViewById(R.id.buttonOpenDrawer);

        // Открываем Navigation Drawer с фрагментами
        buttonOpenDrawer.setOnClickListener(view -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    CatWorldDrawerActivity.class
            );

            startActivity(intent);
        });

        // Ограничиваем функции для гостя
        if (isGuest) {

            buttonAddFavorite.setVisibility(View.GONE);
            buttonShowFavorites.setVisibility(View.GONE);
            buttonDeleteFavorite.setVisibility(View.GONE);

            buttonLogout.setText(
                    "Вернуться к авторизации"
            );
        }

        // Получение списка пород кошек
        buttonGetBreeds.setOnClickListener(view -> {

            List<CatBreed> breeds =
                    getCatBreedsUseCase.execute();

            StringBuilder result = new StringBuilder();

            for (CatBreed breed : breeds) {
                result.append(breed.getName())
                        .append("\n");
            }

            textViewResult.setText(result.toString());
        });

        // Поиск кошек по названию породы
        buttonSearch.setOnClickListener(view -> {

            String query = editTextSearch
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
                    searchCatBreedsUseCase.execute(query);

            if (breeds.isEmpty()) {
                textViewResult.setText(
                        "Порода не найдена"
                );
                return;
            }

            StringBuilder result = new StringBuilder();

            for (CatBreed breed : breeds) {
                result.append(breed.getName())
                        .append("\n");
            }

            textViewResult.setText(result.toString());
        });

        // Показываем карточку бенгальской кошки
        buttonDetails.setOnClickListener(view -> {

            CatBreed breed =
                    getCatDetailsUseCase.execute(5);

            if (breed != null) {

                textViewResult.setText(
                        "Порода: " + breed.getName()
                                + "\n\nОписание: "
                                + breed.getDescription()
                );

            } else {

                textViewResult.setText(
                        "Порода не найдена"
                );
            }
        });

        // Добавляем бенгальскую кошку в избранное
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
                    saveFavoriteUseCase.execute(breed);

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

        // Показываем избранные породы
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
                    new StringBuilder("Избранное:\n");

            for (CatBreed breed : favorites) {
                result.append(breed.getName())
                        .append("\n");
            }

            textViewResult.setText(result.toString());
        });

        // Удаляем бенгальскую кошку из избранного
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

        // Выполняем учебное распознавание породы
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

        // Выход или возврат к авторизации
        buttonLogout.setOnClickListener(view -> {

            if (!isGuest) {
                // Для авторизованного пользователя выполняем выход
                logoutUserUseCase.execute();
            }

            // Открываем экран авторизации
            Intent intent = new Intent(
                    MainActivity.this,
                    AuthActivity.class
            );

            startActivity(intent);

            // Закрываем главный экран
            finish();
        });
    }
}
