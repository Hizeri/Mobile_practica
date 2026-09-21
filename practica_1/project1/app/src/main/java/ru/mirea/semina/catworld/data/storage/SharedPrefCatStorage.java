package ru.mirea.semina.catworld.data.storage;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.semina.catworld.domain.models.CatBreed;

// Хранилище избранных пород через SharedPreferences.
public class SharedPrefCatStorage implements CatStorage {

    // Название файла SharedPreferences.
    private static final String PREFS_NAME =
            "cat_world_preferences";

    // Ключ, по которому хранится избранное.
    private static final String KEY_FAVORITES =
            "favorites";

    private final SharedPreferences sharedPreferences;

    // Конструктор принимает Context.
    public SharedPrefCatStorage(Context context) {

        sharedPreferences =
                context.getSharedPreferences(
                        PREFS_NAME,
                        Context.MODE_PRIVATE
                );
    }

    // Добавление породы в избранное.
    @Override
    public boolean saveFavorite(CatBreed catBreed) {

        List<CatBreed> favorites =
                getFavorites();

        // Проверяем, есть ли эта порода уже в избранном.
        for (CatBreed breed : favorites) {

            if (breed.getId() == catBreed.getId()) {

                // Если уже есть, второй раз не добавляем.
                return false;
            }
        }

        favorites.add(catBreed);

        saveList(favorites);

        return true;
    }

    // Получение избранных пород.
    @Override
    public List<CatBreed> getFavorites() {

        String savedData =
                sharedPreferences.getString(
                        KEY_FAVORITES,
                        ""
                );

        List<CatBreed> favorites =
                new ArrayList<>();

        // Если ничего ещё не сохранено.
        if (savedData == null ||
                savedData.isEmpty()) {

            return favorites;
        }

        // Каждая порода отделяется символом ;
        String[] cats =
                savedData.split(";");

        for (String cat : cats) {

            // Поля породы разделены символом |
            String[] parts =
                    cat.split("\\|");

            if (parts.length >= 3) {

                int id =
                        Integer.parseInt(parts[0]);

                String name =
                        parts[1];

                String description =
                        parts[2];

                CatBreed breed =
                        new CatBreed(
                                id,
                                name,
                                description
                        );

                favorites.add(breed);
            }
        }

        return favorites;
    }

    // Удаление породы из избранного.
    @Override
    public boolean deleteFavorite(int id) {

        List<CatBreed> favorites =
                getFavorites();

        boolean removed =
                favorites.removeIf(
                        breed ->
                                breed.getId() == id
                );

        // Если нашли и удалили породу,
        // сохраняем обновлённый список.
        if (removed) {

            saveList(favorites);
        }

        return removed;
    }

    // Сохранение всего списка в SharedPreferences.
    private void saveList(
            List<CatBreed> favorites
    ) {

        StringBuilder builder =
                new StringBuilder();

        for (CatBreed breed : favorites) {

            builder
                    .append(breed.getId())
                    .append("|")

                    .append(breed.getName())
                    .append("|")

                    .append(breed.getDescription())
                    .append(";");
        }

        sharedPreferences
                .edit()
                .putString(
                        KEY_FAVORITES,
                        builder.toString()
                )
                .apply();
    }
}