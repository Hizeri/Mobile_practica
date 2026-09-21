package ru.mirea.semina.lesson9.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import ru.mirea.semina.lesson9.domain.models.Movie;
import ru.mirea.semina.lesson9.domain.repository.MovieRepository;

// Реализация репозитория.
// Здесь находится работа с Android-хранилищем SharedPreferences.
public class MovieRepositoryImpl implements MovieRepository {

    // Имя файла SharedPreferences.
    private static final String PREFS_NAME = "movie_preferences";

    // Ключ, под которым будет храниться название фильма.
    private static final String KEY_MOVIE_NAME = "favorite_movie_name";

    // Объект для работы с SharedPreferences.
    private final SharedPreferences sharedPreferences;

    // Context передаётся только в data-слой.
    // Domain-слой о Context ничего не знает.
    public MovieRepositoryImpl(Context context) {

        // Создаём или открываем локальное хранилище приложения.
        sharedPreferences = context.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
        );
    }

    @Override
    public boolean saveMovie(Movie movie) {

        // Проверяем, что фильм существует
        // и его название не пустое.
        if (movie == null
                || movie.getName() == null
                || movie.getName().isEmpty()) {
            return false;
        }

        // Сохраняем название фильма в SharedPreferences.
        sharedPreferences
                .edit()
                .putString(KEY_MOVIE_NAME, movie.getName())
                .apply();

        // Если дошли сюда — считаем сохранение успешным.
        return true;
    }

    @Override
    public Movie getMovie() {

        // Получаем сохранённое название фильма.
        // Если ничего ещё не сохранено,
        // будет возвращено значение "Фильм не выбран".
        String movieName = sharedPreferences.getString(
                KEY_MOVIE_NAME,
                "Фильм не выбран"
        );

        // Создаём объект Movie из сохранённых данных.
        return new Movie(1, movieName);
    }
}