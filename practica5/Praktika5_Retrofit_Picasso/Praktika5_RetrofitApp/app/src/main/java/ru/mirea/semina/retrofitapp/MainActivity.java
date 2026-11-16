package ru.mirea.semina.retrofitapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.net.UnknownHostException;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Основной экран учебной практической №5: GET, PATCH, RecyclerView и Picasso.
public class MainActivity extends AppCompatActivity
        implements TodoAdapter.OnTodoCheckedChangeListener {

    private ApiService apiService;
    private TodoAdapter adapter;
    private TextView status;
    private Button refreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Учитываем системные панели (статус-бар и нижнюю навигацию).
        View root = findViewById(R.id.main);
        final int padding = (int) (16 * getResources().getDisplayMetrics().density + 0.5f);
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left + padding, bars.top + padding,
                    bars.right + padding, bars.bottom + padding);
            return insets;
        });

        status = findViewById(R.id.statusText);
        refreshButton = findViewById(R.id.refreshButton);
        RecyclerView list = findViewById(R.id.todoRecyclerView);
        SwitchCompat imageSwitch = findViewById(R.id.imageSwitch);

        // RecyclerView: вертикальные карточки, заполненные адаптером.
        list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TodoAdapter(this);
        list.setAdapter(adapter);

        // Retrofit: базовый адрес + Gson для преобразования JSON в Todo.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        // Кнопка заново выполняет GET-запрос.
        refreshButton.setOnClickListener(v -> loadTodos());

        // Демонстрация настроек Picasso: centerCrop или centerInside.
        imageSwitch.setChecked(true);
        imageSwitch.setOnCheckedChangeListener((buttonView, checked) ->
                adapter.setCropImages(checked));

        // Автоматическая загрузка при открытии приложения.
        loadTodos();
    }

    private void loadTodos() {
        status.setText("Загружаем задачи из интернета…");
        refreshButton.setEnabled(false);

        // enqueue выполняет сетевой запрос асинхронно, не блокируя экран.
        apiService.getTodos().enqueue(new Callback<List<Todo>>() {
            @Override
            public void onResponse(@NonNull Call<List<Todo>> call,
                                   @NonNull Response<List<Todo>> response) {
                refreshButton.setEnabled(true);
                if (response.isSuccessful() && response.body() != null) {
                    List<Todo> todos = response.body();
                    adapter.setItems(todos);
                    status.setText("Получено задач из сети: " + todos.size());
                } else {
                    status.setText("Ошибка сервера: HTTP " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Todo>> call, @NonNull Throwable error) {
                refreshButton.setEnabled(true);
                status.setText(networkError(error));
            }
        });
    }

    @Override
    public void onTodoCheckedChanged(Todo todo, boolean newValue) {
        // Запоминаем исходное состояние на случай ошибки PATCH.
        final int id = todo.getId();
        adapter.setUpdating(id, true);
        status.setText("Отправляем изменение задачи № " + id + "…");

        //  подтверждает PATCH, но не сохраняет его постоянно.
        apiService.updateTodo(id, new TodoUpdate(newValue)).enqueue(new Callback<Todo>() {
            @Override
            public void onResponse(@NonNull Call<Todo> call, @NonNull Response<Todo> response) {
                if (response.isSuccessful()) {
                    // Обновляем состояние в приложении только после успешного ответа.
                    todo.setCompleted(newValue);
                    status.setText("Задача № " + id + " обновлена (демо-сервер)");
                } else {
                    status.setText("Не удалось обновить задачу: HTTP " + response.code());
                    Toast.makeText(MainActivity.this,
                            "Изменение не сохранено", Toast.LENGTH_SHORT).show();
                }
                adapter.setUpdating(id, false);
            }

            @Override
            public void onFailure(@NonNull Call<Todo> call, @NonNull Throwable error) {
                // Значение Todo осталось прежним, поэтому CheckBox вернётся назад.
                adapter.setUpdating(id, false);
                status.setText(networkError(error));
                Toast.makeText(MainActivity.this,
                        "Не удалось отправить изменение", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Понятное сообщение для ошибки DNS и других проблем с сетью.
    private String networkError(Throwable error) {
        if (error instanceof UnknownHostException) {
            return "Нет соединения с сервером. Проверь интернет в эмуляторе.";
        }
        return "Ошибка сети: " + error.getLocalizedMessage();
    }
}
