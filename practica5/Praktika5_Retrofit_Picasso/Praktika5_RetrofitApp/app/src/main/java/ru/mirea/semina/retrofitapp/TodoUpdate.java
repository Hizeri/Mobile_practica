package ru.mirea.semina.retrofitapp;

// Тело PATCH-запроса: отправляем только изменённое поле completed.
public class TodoUpdate {
    private final boolean completed;

    public TodoUpdate(boolean completed) {
        this.completed = completed;
    }
}
