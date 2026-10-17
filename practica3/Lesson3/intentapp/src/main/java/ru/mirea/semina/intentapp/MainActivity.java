
package ru.mirea.semina.intentapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText editNumber;
    private Button buttonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editNumber = findViewById(R.id.editNumber);
        buttonSend = findViewById(R.id.buttonSend);

        buttonSend.setOnClickListener(v -> {
            String input = editNumber.getText().toString().trim();

            if (input.isEmpty()) {
                Toast.makeText(
                        this,
                        "Введите номер по списку",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            int number;

            try {
                number = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                Toast.makeText(
                        this,
                        "Введите корректный номер",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            long square = (long) number * number;

            long dateInMillis = System.currentTimeMillis();
            String format = "yyyy-MM-dd HH:mm:ss";

            SimpleDateFormat sdf = new SimpleDateFormat(
                    format,
                    Locale.getDefault()
            );

            String dateString = sdf.format(new Date(dateInMillis));

            Intent intent = new Intent(
                    MainActivity.this,
                    SecondActivity.class
            );

            intent.putExtra("square", square);
            intent.putExtra("time", dateString);

            startActivity(intent);
        });
    }
}
