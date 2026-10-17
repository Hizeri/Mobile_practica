
package ru.mirea.semina.favoritebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Ключи для передачи данных между Activity
    public static final String BOOK_NAME_KEY = "book_name";
    public static final String QUOTES_KEY = "quotes_name";

    private TextView textViewBook;

    // Получаем результат из второй Activity
    private final ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {

                        if (result.getResultCode() == Activity.RESULT_OK
                                && result.getData() != null) {

                            Intent data = result.getData();

                            // Читаем название книги и цитату
                            String book = data.getStringExtra(BOOK_NAME_KEY);
                            String quote = data.getStringExtra(QUOTES_KEY);

                            // Выводим результат на первый экран
                            textViewBook.setText(
                                    "Название Вашей любимой книги: "
                                            + book
                                            + "\n\nЦитата: "
                                            + quote
                            );
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewBook = findViewById(R.id.textViewBook);

        Button buttonOpen = findViewById(R.id.buttonOpen);

        // Переход на второй экран с ожиданием результата
        buttonOpen.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    ShareActivity.class
            );

            activityResultLauncher.launch(intent);
        });
    }
}
