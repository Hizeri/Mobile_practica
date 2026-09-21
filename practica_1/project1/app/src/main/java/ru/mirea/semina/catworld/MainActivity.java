package ru.mirea.semina.catworld;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import ru.mirea.semina.catworld.data.repository.CatRepositoryImpl;
import ru.mirea.semina.catworld.data.storage.CatStorage;
import ru.mirea.semina.catworld.data.storage.SharedPrefCatStorage;
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

    private GetCatBreedsUseCase getCatBreedsUseCase;
    private SearchCatBreedsUseCase searchCatBreedsUseCase;
    private GetCatDetailsUseCase getCatDetailsUseCase;
    private SaveFavoriteUseCase saveFavoriteUseCase;
    private GetFavoritesUseCase getFavoritesUseCase;
    private DeleteFavoriteUseCase deleteFavoriteUseCase;
    private RecognizeCatBreedUseCase recognizeCatBreedUseCase;

    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Создаём хранилище избранного.
        CatStorage catStorage =
                new SharedPrefCatStorage(this);

        // Создаём репозиторий.
        CatRepository catRepository =
                new CatRepositoryImpl(catStorage);

        // Создаём UseCase.
        getCatBreedsUseCase =
                new GetCatBreedsUseCase(catRepository);

        searchCatBreedsUseCase =
                new SearchCatBreedsUseCase(catRepository);

        getCatDetailsUseCase =
                new GetCatDetailsUseCase(catRepository);

        saveFavoriteUseCase =
                new SaveFavoriteUseCase(catRepository);

        getFavoritesUseCase =
                new GetFavoritesUseCase(catRepository);

        deleteFavoriteUseCase =
                new DeleteFavoriteUseCase(catRepository);

        recognizeCatBreedUseCase =
                new RecognizeCatBreedUseCase(catRepository);

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

        // Показать все породы.
        buttonGetBreeds.setOnClickListener(view -> {

            List<CatBreed> breeds =
                    getCatBreedsUseCase.execute();

            StringBuilder result =
                    new StringBuilder();

            for (CatBreed breed : breeds) {
                result.append(breed.getName())
                        .append("\n");
            }

            textViewResult.setText(
                    result.toString()
            );
        });

        // Поиск породы.
        buttonSearch.setOnClickListener(view -> {

            String query =
                    editTextSearch
                            .getText()
                            .toString()
                            .trim();

            List<CatBreed> breeds =
                    searchCatBreedsUseCase.execute(query);

            if (breeds.isEmpty()) {

                textViewResult.setText(
                        "Порода не найдена"
                );

            } else {

                StringBuilder result =
                        new StringBuilder();

                for (CatBreed breed : breeds) {
                    result.append(breed.getName())
                            .append("\n");
                }

                textViewResult.setText(
                        result.toString()
                );
            }
        });

        // Показать карточку Бенгальской кошки.
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

        // Добавить в избранное.
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

        // Показать избранное.
        buttonShowFavorites.setOnClickListener(view -> {

            List<CatBreed> favorites =
                    getFavoritesUseCase.execute();

            if (favorites.isEmpty()) {

                textViewResult.setText(
                        "Избранное пусто"
                );

            } else {

                StringBuilder result =
                        new StringBuilder("Избранное:\n");

                for (CatBreed breed : favorites) {
                    result.append(breed.getName())
                            .append("\n");
                }

                textViewResult.setText(
                        result.toString()
                );
            }
        });

        // Удалить из избранного.
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

        // Распознавание породы.
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
    }
}