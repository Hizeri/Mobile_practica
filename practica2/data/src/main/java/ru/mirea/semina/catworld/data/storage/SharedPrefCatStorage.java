package ru.mirea.semina.catworld.data.storage;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.semina.catworld.domain.models.CatBreed;

// Старое хранилище через SharedPreferences.
public class SharedPrefCatStorage implements CatStorage {

    private static final String PREFS_NAME =
            "cat_world_preferences";

    private static final String KEY_FAVORITES =
            "favorites";

    private final SharedPreferences sharedPreferences;

    public SharedPrefCatStorage(Context context) {

        // Получаем SharedPreferences приложения.
        sharedPreferences =
                context.getSharedPreferences(
                        PREFS_NAME,
                        Context.MODE_PRIVATE
                );
    }

    @Override
    public boolean saveFavorite(CatBreed catBreed) {

        // Получаем текущий список.
        List<CatBreed> favorites =
                getFavorites();

        // Проверяем наличие породы.
        for (CatBreed breed : favorites) {

            if (breed.getId() == catBreed.getId()) {
                return false;
            }
        }

        // Добавляем новую породу.
        favorites.add(catBreed);

        saveList(favorites);

        return true;
    }

    @Override
    public List<CatBreed> getFavorites() {

        // Читаем сохранённую строку.
        String savedData =
                sharedPreferences.getString(
                        KEY_FAVORITES,
                        ""
                );

        List<CatBreed> favorites =
                new ArrayList<>();

        if (savedData == null || savedData.isEmpty()) {
            return favorites;
        }

        // Разделяем сохранённые записи.
        String[] cats =
                savedData.split(";");

        for (String cat : cats) {

            String[] parts =
                    cat.split("\\|");

            if (parts.length >= 3) {

                int id =
                        Integer.parseInt(parts[0]);

                String name =
                        parts[1];

                String description =
                        parts[2];

                favorites.add(
                        new CatBreed(
                                id,
                                name,
                                description
                        )
                );
            }
        }

        return favorites;
    }

    @Override
    public boolean deleteFavorite(int id) {

        // Получаем список избранного.
        List<CatBreed> favorites =
                getFavorites();

        // Удаляем породу с нужным id.
        boolean removed =
                favorites.removeIf(
                        breed ->
                                breed.getId() == id
                );

        if (removed) {
            saveList(favorites);
        }

        return removed;
    }

    // Сохраняем список в SharedPreferences.
    private void saveList(List<CatBreed> favorites) {

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