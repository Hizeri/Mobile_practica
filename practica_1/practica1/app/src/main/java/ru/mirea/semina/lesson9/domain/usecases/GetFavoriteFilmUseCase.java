package ru.mirea.semina.lesson9.domain.usecases;

import ru.mirea.semina.lesson9.domain.models.Movie;
import ru.mirea.semina.lesson9.domain.repository.MovieRepository;

// Use Case для получения любимого фильма.
public class GetFavoriteFilmUseCase {

    // Репозиторий передаётся извне.
    private MovieRepository movieRepository;

    // Сохраняем переданный репозиторий.
    public GetFavoriteFilmUseCase(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    // Получаем фильм через репозиторий.
    public Movie execute() {
        return movieRepository.getMovie();
    }
}