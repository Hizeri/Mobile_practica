
package ru.mirea.semina.favoritebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ShareActivity extends AppCompatActivity {

    private EditText editBook;
    private EditText editQuote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        editBook = findViewById(R.id.editBook);
        editQuote = findViewById(R.id.editQuote);

        Button buttonSend = findViewById(R.id.buttonSend);

        // Отправка данных на первый экран
        buttonSend.setOnClickListener(v -> {

            String book = editBook.getText().toString().trim();
            String quote = editQuote.getText().toString().trim();

            // Проверяем, что оба поля заполнены
            if (book.isEmpty() || quote.isEmpty()) {
                Toast.makeText(
                        this,
                        "Заполните оба поля",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            // Создаём Intent с результатом
            Intent resultIntent = new Intent();

            resultIntent.putExtra(
                    MainActivity.BOOK_NAME_KEY,
                    book
            );

            resultIntent.putExtra(
                    MainActivity.QUOTES_KEY,
                    quote
            );

            // Сообщаем, что данные успешно переданы
            setResult(Activity.RESULT_OK, resultIntent);

            // Закрываем второй экран и возвращаемся на первый
            finish();
        });
    }
}
