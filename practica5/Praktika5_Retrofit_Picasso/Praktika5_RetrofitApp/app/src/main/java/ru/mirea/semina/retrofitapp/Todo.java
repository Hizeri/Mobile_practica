package ru.mirea.semina.retrofitapp;

// Модель одного задания из JSONPlaceholder.
// Gson автоматически заполняет эти поля из одноимённых ключей JSON.
public class Todo {
    private int userId;          // Номер пользователя.
    private int id;              // Уникальный номер задачи.
    private String title;        // Название задачи с сервера.
    private boolean completed;   // Выполнена задача или нет.

    public int getUserId() { return userId; }
    public int getId() { return id; }
    public String getTitle() { return title; }
    public boolean isCompleted() { return completed; }

    // Обновляем локальное значение после успешного PATCH-запроса.
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
