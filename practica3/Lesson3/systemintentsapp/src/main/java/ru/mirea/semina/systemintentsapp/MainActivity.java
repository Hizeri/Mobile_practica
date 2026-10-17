
package ru.mirea.semina.systemintentsapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Находим кнопки в XML-разметке
        Button buttonCall = findViewById(R.id.buttonCall);
        Button buttonBrowser = findViewById(R.id.buttonBrowser);
        Button buttonMap = findViewById(R.id.buttonMap);

        // Назначаем действия при нажатии
        buttonCall.setOnClickListener(v -> onClickCall());
        buttonBrowser.setOnClickListener(v -> onClickOpenBrowser());
        buttonMap.setOnClickListener(v -> onClickOpenMaps());
    }

    // Открываем приложение для набора номера
    public void onClickCall() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:89811112233"));
        openSystemApp(intent);
    }

    // Открываем сайт Android Developers в браузере
    public void onClickOpenBrowser() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://developer.android.com"));
        openSystemApp(intent);
    }

    // Открываем координаты на карте
    public void onClickOpenMaps() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("geo:55.749479,37.613944"));
        openSystemApp(intent);
    }

    // Проверяем, есть ли приложение для обработки Intent
    private void openSystemApp(Intent intent) {
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(
                    this,
                    "На устройстве нет подходящего приложения",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}
