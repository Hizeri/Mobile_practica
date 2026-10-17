
package ru.mirea.semina.intentapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    private TextView textResult;
    private Button buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        textResult = findViewById(R.id.textResult);
        buttonBack = findViewById(R.id.buttonBack);

        Intent intent = getIntent();

        long square = intent.getLongExtra("square", 0);
        String time = intent.getStringExtra("time");

        if (time == null) {
            time = "не определено";
        }

        String result =
                "КВАДРАТ ЗНАЧЕНИЯ МОЕГО НОМЕРА " +
                        "ПО СПИСКУ В ГРУППЕ СОСТАВЛЯЕТ " +
                        square +
                        ", а текущее время " +
                        time;

        textResult.setText(result);

        // Обработка нажатия кнопки "Назад"
        buttonBack.setOnClickListener(v -> {
            finish();
        });
    }
}
