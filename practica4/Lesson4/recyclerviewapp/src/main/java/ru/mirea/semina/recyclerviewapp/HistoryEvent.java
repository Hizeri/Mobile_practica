
package ru.mirea.semina.recyclerviewapp;

// Модель данных одного исторического события
public class HistoryEvent {

    // Год исторического события
    public final String year;

    // Название события
    public final String title;

    // Краткое описание события
    public final String description;

    // Идентификатор фотографии из папки drawable
    public final int imageRes;

    // Конструктор создаёт объект исторического события
    public HistoryEvent(
            String year,
            String title,
            String description,
            int imageRes
    ) {

        // Сохраняем переданные данные
        this.year = year;
        this.title = title;
        this.description = description;
        this.imageRes = imageRes;
    }
}
