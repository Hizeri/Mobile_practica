
package ru.mirea.semina.catworld.domain.models;

// Модель одной породы кошки.
// Хранит информацию, которую использует приложение.
public class CatBreed {

    // Числовой ID породы.
    private int id;
    private String name;
    private String description;
    private String imageUrl;

    public CatBreed(int id, String name, String description) {

        this.id = id;
        this.name = name;
        this.description = description;

        // Если ссылку не передали, фотографии из сети нет.
        this.imageUrl = null;
    }

    public CatBreed(
            int id,
            String name,
            String description,
            String imageUrl
    ) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    // Получаем ID породы.
    public int getId() {
        return id;
    }

    // Получаем название породы.
    public String getName() {
        return name;
    }

    // Получаем описание породы.
    public String getDescription() {
        return description;
    }

    // получаем ссылку на фотографию.
    public String getImageUrl() {
        return imageUrl;
    }
}
