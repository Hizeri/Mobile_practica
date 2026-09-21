package ru.mirea.semina.lesson9.domain.repository;

import ru.mirea.semina.lesson9.domain.models.Movie;

// Интерфейс репозитория.
// Он описывает, какие операции с фильмами доступны,
// но не знает, как именно они реализованы.
public interface MovieRepository {

    // Сохранить фильм.
    boolean saveMovie(Movie movie);

    // Получить любимый фильм.
    Movie getMovie();
}