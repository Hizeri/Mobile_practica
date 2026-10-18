package ru.mirea.semina.catworld;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

// Activity теперь только отображает результат и передаёт нажатия в ViewModel.
public class MainActivity extends AppCompatActivity {
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ViewModelProvider сохраняет ViewModel при обычном повороте устройства.
        viewModel = new ViewModelProvider(this, new MainViewModelFactory(this))
                .get(MainViewModel.class);
        boolean guest = getIntent().getBooleanExtra("IS_GUEST", false);
        viewModel.setGuest(guest);

        EditText search = findViewById(R.id.editTextSearch);
        TextView result = findViewById(R.id.textViewResult);
        TextView combined = findViewById(R.id.textViewCombined);
        Button add = findViewById(R.id.buttonAddFavorite);
        Button show = findViewById(R.id.buttonShowFavorites);
        Button remove = findViewById(R.id.buttonDeleteFavorite);
        Button logout = findViewById(R.id.buttonLogout);

        // observe автоматически обновляет UI после изменения LiveData.
        viewModel.getResult().observe(this, result::setText);
        viewModel.getCombinedData().observe(this, combined::setText);

        // Кнопки теперь не вызывают UseCase напрямую.
        findViewById(R.id.buttonGetBreeds).setOnClickListener(v -> viewModel.showBreeds());
        findViewById(R.id.buttonSearch).setOnClickListener(v -> viewModel.search(search.getText().toString()));
        findViewById(R.id.buttonDetails).setOnClickListener(v -> viewModel.showDetails());
        add.setOnClickListener(v -> viewModel.addFavorite());
        show.setOnClickListener(v -> viewModel.showFavorites());
        remove.setOnClickListener(v -> viewModel.removeFavorite());
        findViewById(R.id.buttonRecognize).setOnClickListener(v -> viewModel.recognize());

        // Кнопка для демонстрации MediatorLiveData: сеть + Room.
        findViewById(R.id.buttonRefreshSources).setOnClickListener(v -> viewModel.refreshCombinedData());

        // Для гостя избранное недоступно, как и в предыдущих практических.
        if (guest) {
            add.setVisibility(View.GONE);
            show.setVisibility(View.GONE);
            remove.setVisibility(View.GONE);
            logout.setText("Вернуться к авторизации");
        }

        logout.setOnClickListener(v -> {
            viewModel.logout();
            startActivity(new Intent(this, AuthActivity.class));
            finish();
        });
    }
}
