package ru.mirea.semina.lesson9;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ru.mirea.semina.lesson9.data.repository.MovieRepositoryImpl;
import ru.mirea.semina.lesson9.domain.models.Movie;
import ru.mirea.semina.lesson9.domain.repository.MovieRepository;
import ru.mirea.semina.lesson9.domain.usecases.GetFavoriteFilmUseCase;
import ru.mirea.semina.lesson9.domain.usecases.SaveFilmToFavoriteUseCase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Подключаем разметку экрана.
        setContentView(R.layout.activity_main);

        // Получаем элементы интерфейса из XML.
        EditText text = findViewById(R.id.editTextMovie);
        TextView textView = findViewById(R.id.textViewMovie);

        // Создаём реализацию репозитория.
        // MainActivity знает о data-слое и передаёт репозиторий в domain.
        MovieRepository movieRepository = new MovieRepositoryImpl(this);

        // Обработчик кнопки сохранения фильма.
        findViewById(R.id.buttonSaveMovie).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // Создаём объект Movie из введённого названия.
                        Movie movie = new Movie(
                                2,
                                text.getText().toString()
                        );

                        // Создаём Use Case и передаём ему репозиторий.
                        SaveFilmToFavoriteUseCase saveUseCase =
                                new SaveFilmToFavoriteUseCase(movieRepository);

                        // Выполняем сохранение фильма.
                        boolean result = saveUseCase.execute(movie);

                        // Показываем результат на экране.
                        textView.setText(
                                String.format("Save result: %s", result)
                        );
                    }
                }
        );

        // Обработчик кнопки получения любимого фильма.
        findViewById(R.id.buttonGetMovie).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // Создаём Use Case и передаём ему репозиторий.
                        GetFavoriteFilmUseCase getUseCase =
                                new GetFavoriteFilmUseCase(movieRepository);

                        // Получаем фильм через репозиторий.
                        Movie movie = getUseCase.execute();

                        // Показываем название фильма.
                        textView.setText(
                                String.format(
                                        "Favorite movie: %s",
                                        movie.getName()
                                )
                        );
                    }
                }
        );
    }
}