
package ru.mirea.semina.recyclerviewapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// Адаптер связывает данные с карточками RecyclerView
public class HistoryAdapter
        extends RecyclerView.Adapter<HistoryAdapter.EventHolder> {

    // Список исторических событий
    private final List<HistoryEvent> events;

    // Получаем список событий из MainActivity
    public HistoryAdapter(List<HistoryEvent> events) {
        this.events = events;
    }

    @NonNull
    @Override
    public EventHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        // Превращаем XML-разметку в элемент интерфейса
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(
                        R.layout.item_event,
                        parent,
                        false
                );

        // Создаём объект для хранения элементов карточки
        return new EventHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull EventHolder holder,
            int position
    ) {

        // Получаем событие по позиции в списке
        HistoryEvent event = events.get(position);

        // Отображаем год и название события
        holder.title.setText(
                event.year + " — " + event.title
        );

        // Показываем описание события
        holder.description.setText(event.description);

        // Загружаем соответствующую фотографию
        holder.image.setImageResource(event.imageRes);

        // Добавляем описание изображения для доступности
        holder.image.setContentDescription(
                "Изображение: " + event.title
        );
    }

    @Override
    public int getItemCount() {

        // Возвращаем количество исторических событий
        return events.size();
    }

    // ViewHolder хранит ссылки на элементы одной карточки
    static class EventHolder extends RecyclerView.ViewHolder {

        // Фотография исторического события
        final ImageView image;

        // Заголовок карточки
        final TextView title;

        // Описание исторического события
        final TextView description;

        EventHolder(@NonNull View itemView) {
            super(itemView);

            // Находим фотографию в XML-разметке
            image = itemView.findViewById(R.id.eventImage);

            // Находим текст заголовка
            title = itemView.findViewById(R.id.eventTitle);

            // Находим текст описания
            description = itemView.findViewById(
                    R.id.eventDescription
            );
        }
    }
}
