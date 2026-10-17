
package ru.mirea.semina.sharer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        TextView textReceived = findViewById(R.id.textReceived);

        // Получаем Intent, который запустил Activity
        Intent intent = getIntent();

        String action = intent.getAction();
        String type = intent.getType();

        // Проверяем, что данные отправлены через ACTION_SEND
        if (Intent.ACTION_SEND.equals(action) && type != null) {

            if ("text/plain".equals(type)) {

                // Получаем переданный текст
                String text = intent.getStringExtra(
                        Intent.EXTRA_TEXT
                );

                textReceived.setText(
                        "Полученный текст:\n" +
                                (text != null ? text : "Текст отсутствует")
                );

            } else if (type.startsWith("image/")) {

                // Получаем адрес переданного изображения
                Uri imageUri = intent.getParcelableExtra(
                        Intent.EXTRA_STREAM
                );

                textReceived.setText(
                        "Получено изображение:\n" +
                                (imageUri != null
                                        ? imageUri.toString()
                                        : "Изображение отсутствует")
                );
            } else {
                textReceived.setText("Неподдерживаемый тип данных");
            }

        } else {
            textReceived.setText("Данные пока не получены");
        }
    }
}
