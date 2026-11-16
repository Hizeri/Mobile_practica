
package ru.mirea.semina.catworld;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

// Экран каталога пород кошек.
public class BreedListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Подключаем разметку каталога.
        setContentView(R.layout.activity_breed_list);

        // Находим RecyclerView.
        RecyclerView list =
                findViewById(R.id.breedsRecyclerView);

        // Находим текст для количества пород и ошибок.
        TextView empty =
                findViewById(R.id.emptyBreeds);

        // Карточки располагаются вертикально.
        list.setLayoutManager(new LinearLayoutManager(this));

        // ! создаём адаптер с обработчиком нажатий.
        CatBreedAdapter adapter = new CatBreedAdapter(breed -> {

            // Создаём намерение открыть экран подробностей.
            Intent intent = new Intent(BreedListActivity.this, BreedDetailsActivity.class);

            // Передаём ID выбранной породы.
            intent.putExtra(BreedDetailsActivity.EXTRA_BREED_ID, breed.getId());

            // Передаём название.
            intent.putExtra(BreedDetailsActivity.EXTRA_BREED_NAME, breed.getName());

            // Передаём описание.
            intent.putExtra(BreedDetailsActivity.EXTRA_BREED_DESCRIPTION, breed.getDescription());

            // Передаём интернет-ссылку на фотографию.
            intent.putExtra(BreedDetailsActivity.EXTRA_BREED_IMAGE_URL, breed.getImageUrl());

            // Открываем страницу выбранной породы.
            startActivity(intent);
        });

        // Подключаем адаптер к RecyclerView.
        list.setAdapter(adapter);

        // Получаем ViewModel через фабрику.
        MainViewModel vm = new ViewModelProvider(
                this,
                new MainViewModelFactory(this)
        ).get(MainViewModel.class);

        // Наблюдаем за списком пород.
        vm.getBreedItems().observe(this, breeds -> {

            // Передаём список в адаптер.
            adapter.setItems(breeds);

            // Показываем количество пород.
            if (breeds != null && !breeds.isEmpty()) {
                empty.setText(
                        "Найдено пород: " + breeds.size()
                );
            }
        });

        // Наблюдаем за загрузкой и ошибками.
        vm.getBreedLoadStatus().observe(this, status -> {

            if (status != null && !status.isEmpty()) {
                empty.setText(status);
            }
        });

        // Загружаем список при первом открытии.
        if (vm.getBreedItems().getValue() == null) {
            vm.loadBreedItems();
        }
    }

    // Кнопка "Назад" возвращает на главный экран.
    public void goBackToCatWorld(View view) {
        finish();
    }
}
