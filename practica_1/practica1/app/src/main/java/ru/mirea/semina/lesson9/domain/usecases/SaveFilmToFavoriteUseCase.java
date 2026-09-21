package ru.mirea.semina.lesson9.domain.usecases;

import ru.mirea.semina.lesson9.domain.models.Movie;
import ru.mirea.semina.lesson9.domain.repository.MovieRepository;

// Use Case для сохранения фильма в избранное.
public class SaveFilmToFavoriteUseCase {

    // Репозиторий, через который будет выполняться сохранение.
    private MovieRepository movieRepository;

    public SaveFilmToFavoriteUseCase(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    // Передаём фильм репозиторию на сохранение.
    public boolean execute(Movie movie) {
        return movieRepository.saveMovie(movie);
    }
}