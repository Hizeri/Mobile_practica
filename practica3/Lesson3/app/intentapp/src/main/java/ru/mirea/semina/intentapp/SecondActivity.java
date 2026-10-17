
package ru.mirea.semina.intentapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        TextView textResult = findViewById(R.id.textResult);

        Intent intent = getIntent();

        long square = intent.getLongExtra("square", 0);
        String time = intent.getStringExtra("time");

        String result =
                "КВАДРАТ ЗНАЧЕНИЯ МОЕГО НОМЕРА " +
                        "ПО СПИСКУ В ГРУППЕ СОСТАВЛЯЕТ " +
                        square + ", а текущее время " + time;

        textResult.setText(result);
    }
}
