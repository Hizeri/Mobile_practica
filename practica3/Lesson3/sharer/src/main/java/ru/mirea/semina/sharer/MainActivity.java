
package ru.mirea.semina.sharer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView textResult;

    // Получаем результат из другого приложения
    private final ActivityResultLauncher<Intent> imageLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {

                        if (result.getResultCode() == Activity.RESULT_OK
                                && result.getData() != null) {

                            Intent data = result.getData();

                            if (data.getData() != null) {
                                String uri = data.getData().toString();

                                // Показываем адрес выбранного изображения
                                textResult.setText(
                                        "Выбрано изображение:\n" + uri
                                );
                            }
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonShare = findViewById(R.id.buttonShare);
        Button buttonPick = findViewById(R.id.buttonPick);
        TextView title = findViewById(R.id.textTitle);

        textResult = findViewById(R.id.textResult);

        title.setText("Практическая №3 — Sharer");

        // Кнопка отправки текста
        buttonShare.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_SEND);

            // Указываем тип отправляемых данных
            intent.setType("text/plain");

            // Передаём текст через Intent
            intent.putExtra(
                    Intent.EXTRA_TEXT,
                    "Привет из CatWorld! Мне нравятся кошки!"
            );

            // Открываем системное меню выбора приложения
            Intent chooser = Intent.createChooser(
                    intent,
                    "Выбор за вами!"
            );

            startActivity(chooser);
        });

        // Кнопка выбора изображения
        buttonPick.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_PICK);

            // Разрешаем выбирать изображения
            intent.setType("image/*");

            try {
                // Запускаем Activity с ожиданием результата
                imageLauncher.launch(intent);
            } catch (Exception e) {
                Toast.makeText(
                        this,
                        "Нет приложения для выбора изображений",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}
