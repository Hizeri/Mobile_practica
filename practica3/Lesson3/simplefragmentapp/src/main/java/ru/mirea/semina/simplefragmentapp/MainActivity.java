
package ru.mirea.semina.simplefragmentapp;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Получаем менеджер для работы с фрагментами
        fragmentManager = getSupportFragmentManager();

        // Находим кнопки вертикальной ориентации
        Button btnFirst = findViewById(R.id.btnFirstFragment);
        Button btnSecond = findViewById(R.id.btnSecondFragment);

        // Если кнопок нет, значит открыта горизонтальная разметка
        boolean isLandscape = (btnFirst == null);

        if (!isLandscape) {

            // Кнопка показывает первый фрагмент
            btnFirst.setOnClickListener(v ->
                    showFragment(new FirstFragment())
            );

            // Кнопка показывает второй фрагмент
            btnSecond.setOnClickListener(v ->
                    showFragment(new SecondFragment())
            );
        }

        // Удаляем старые фрагменты после смены ориентации
        if (savedInstanceState != null) {
            FragmentTransaction transaction =
                    fragmentManager.beginTransaction();

            for (Fragment fragment : fragmentManager.getFragments()) {
                transaction.remove(fragment);
            }

            transaction.commitNow();
        }

        if (isLandscape) {

            // В горизонтальной ориентации показываем оба фрагмента
            fragmentManager.beginTransaction()
                    .replace(
                            R.id.fragmentContainerView,
                            new FirstFragment()
                    )
                    .replace(
                            R.id.secondContainer,
                            new SecondFragment()
                    )
                    .commit();

        } else {

            // В вертикальной ориентации показываем первый фрагмент
            showFragment(new FirstFragment());
        }
    }

    // Метод для переключения фрагментов
    private void showFragment(Fragment fragment) {

        fragmentManager.beginTransaction()
                .replace(
                        R.id.fragmentContainerView,
                        fragment
                )
                .commit();
    }
}
