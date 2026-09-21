package ru.mirea.semina.lesson9.domain.models;

// Сущность Movie.
// Хранит основные данные о фильме.
public class Movie {

    // Уникальный идентификатор фильма.
    private int id;

    // Название фильма.
    private String name;

    // Конструктор создаёт объект фильма
    // и сразу задаёт ему id и название.
    public Movie(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Возвращает id фильма.
    public int getId() {
        return id;
    }

    // Возвращает название фильма.
    public String getName() {
        return name;
    }
}