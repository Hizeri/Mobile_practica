package ru.mirea.semina.scrollviewapp;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.math.BigInteger;

// Учебный модуль 1.1: 100 элементов геометрической прогрессии.
public class MainActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_main);
        // ScrollView содержит единственный LinearLayout для строк списка.
        LinearLayout wrapper = findViewById(R.id.wrapper);
        // BigInteger нужен, чтобы значения 2^99 не переполнили int или long.
        BigInteger value = BigInteger.ONE;
        for (int i = 1; i <= 100; i++) {
            // Отдельную строку создаём из item.xml через LayoutInflater.
            View row = getLayoutInflater().inflate(R.layout.item, wrapper, false);
            TextView text = row.findViewById(R.id.itemText);
            text.setText("Элемент " + i + ": " + value);
            wrapper.addView(row);
            // Знаменатель прогрессии q=2.
            value = value.multiply(BigInteger.valueOf(2));
        }
    }
}
