package ru.mirea.semina.catworld.data.storage;

import android.content.Context;
import android.content.SharedPreferences;

// Хранилище информации о клиенте через SharedPreferences.
public class SharedPrefClientStorage {

    private static final String PREFS_NAME =
            "cat_world_client";

    private static final String KEY_EMAIL =
            "client_email";

    private final SharedPreferences sharedPreferences;

    public SharedPrefClientStorage(Context context) {

        // Получаем локальное хранилище приложения.
        sharedPreferences =
                context.getSharedPreferences(
                        PREFS_NAME,
                        Context.MODE_PRIVATE
                );
    }

    // Сохраняем e-mail клиента.
    public void saveEmail(String email) {

        sharedPreferences
                .edit()
                .putString(KEY_EMAIL, email)
                .apply();
    }

    // Получаем сохранённый e-mail.
    public String getEmail() {

        return sharedPreferences.getString(
                KEY_EMAIL,
                ""
        );
    }

    // Удаляем сохранённый e-mail.
    public void clearClient() {

        sharedPreferences
                .edit()
                .remove(KEY_EMAIL)
                .apply();
    }
}