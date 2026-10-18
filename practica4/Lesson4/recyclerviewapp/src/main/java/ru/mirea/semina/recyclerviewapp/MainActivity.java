
package ru.mirea.semina.recyclerviewapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Вызываем стандартный метод создания Activity
        super.onCreate(savedInstanceState);

        // Подключаем главный экран приложения
        setContentView(R.layout.activity_main);

        // Создаём список исторических событий
        List<HistoryEvent> events = new ArrayList<>();

        // Первое событие — полёт Гагарина
        events.add(new HistoryEvent(
                "1961",
                "Первый полёт человека в космос",
                "Юрий Гагарин совершил орбитальный полёт " +
                        "на корабле «Восток-1».",
                R.drawable.gagarin
        ));

        // Второе событие — высадка на Луну
        events.add(new HistoryEvent(
                "1969",
                "Высадка на Луну",
                "Нил Армстронг и Базз Олдрин " +
                        "ступили на поверхность Луны.",
                R.drawable.moon
        ));

        // Третье событие — падение Берлинской стены
        events.add(new HistoryEvent(
                "1989",
                "Падение Берлинской стены",
                "Открытие границы стало символом " +
                        "окончания разделения Берлина.",
                R.drawable.berlin
        ));

        // Четвёртое событие — окончание войны
        events.add(new HistoryEvent(
                "1945",
                "Окончание Второй мировой войны",
                "Завершился крупнейший военный " +
                        "конфликт XX века.",
                R.drawable.war1945
        ));

        // Пятое событие — появление Всемирной паутины
        events.add(new HistoryEvent(
                "1991",
                "Появление Всемирной паутины",
                "Тим Бернерс-Ли сделал веб-технологию " +
                        "доступной для первых пользователей.",
                R.drawable.internet
        ));

        // Находим RecyclerView на экране
        RecyclerView recyclerView =
                findViewById(R.id.recyclerView);

        // Располагаем карточки вертикально
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        // Создаём адаптер и передаём ему список событий
        HistoryAdapter adapter = new HistoryAdapter(events);

        // Подключаем адаптер к RecyclerView
        recyclerView.setAdapter(adapter);
    }
}
