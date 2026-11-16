
package ru.mirea.semina.catworld.data.network;

import com.google.gson.annotations.SerializedName;

// Модель данных, которые приходят с сервера The Cat API.Описывает данные одной породы
public class CatApiBreedDto {

    // Уникальный ID породы на сервере.
    @SerializedName("id")
    private String id;

    // Название породы.
    @SerializedName("name")
    private String name;

    // Описание породы.
    @SerializedName("description")
    private String description;

    // Информация о фотографии породы.
    @SerializedName("image")
    private CatImage image;

    // Получаем ID породы.
    public String getId() {
        return id;
    }

    // Получаем название.
    public String getName() {
        return name;
    }

    // Получаем описание.
    public String getDescription() {
        return description;
    }

    // Получаем ссылку на фотографию.
    public String getImageUrl() {

        // Если сервер не прислал изображение,возвращаем null.
        if (image == null) {
            return null;
        }

        return image.url;
    }

    // Вложенный класс для объекта image из JSON.
    public static class CatImage {

        // Интернет-адрес фотографии.
        @SerializedName("url")
        private String url;
    }
}
