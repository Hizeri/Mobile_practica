
package ru.mirea.semina.catworld;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

// Экран со списком пород кошек
public class BreedListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Загружаем разметку экрана
        setContentView(R.layout.activity_breed_list);

        // Находим список пород
        RecyclerView recyclerView =
                findViewById(R.id.breedsRecyclerView);

        // Находим текст с количеством пород
        TextView emptyBreeds =
                findViewById(R.id.emptyBreeds);

        // Находим кнопку возврата
        Button buttonBack =
                findViewById(R.id.buttonBack);

        // Обрабатываем нажатие кнопки Назад
        buttonBack.setOnClickListener(v -> {

            // Закрываем текущий экран каталога
            // Android вернёт пользователя в MainActivity
            finish();
        });

        // Располагаем карточки вертикально
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        // Создаём адаптер для карточек кошек
        CatBreedAdapter adapter = new CatBreedAdapter();

        // Подключаем адаптер к списку
        recyclerView.setAdapter(adapter);

        // Создаём ViewModel через фабрику
        MainViewModel viewModel = new ViewModelProvider(
                this,
                new MainViewModelFactory(this)
        ).get(MainViewModel.class);

        // Наблюдаем за списком пород через LiveData
        viewModel.getBreedItems().observe(this, breeds -> {

            // Обновляем карточки при получении данных
            adapter.setItems(breeds);

            // Показываем количество пород
            if (breeds == null || breeds.isEmpty()) {

                emptyBreeds.setText("Список пуст");

            } else {

                emptyBreeds.setText(
                        "Найдено пород: " + breeds.size()
                );
            }
        });

        // Загружаем породы, если данных ещё нет
        if (viewModel.getBreedItems().getValue() == null) {
            viewModel.loadBreedItems();
        }
    }
}
