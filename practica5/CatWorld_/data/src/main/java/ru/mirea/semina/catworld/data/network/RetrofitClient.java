
package ru.mirea.semina.catworld.data.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Класс для настройки подключения Retrofit к серверу.
public class RetrofitClient {

    // Основной адрес сервера The Cat API.
    private static final String BASE_URL = "https://api.thecatapi.com/";

    // Храним один экземпляр Retrofit для всего приложения.
    private static Retrofit retrofit;

    // Метод возвращает настроенный Retrofit.
    public static Retrofit getClient() {

        // Если объект ещё не создан, создаём его.
        if (retrofit == null) {
// Указываем адрес сервера.// Подключаем Gson для обработки JSON.// Создаём объект Retrofit.
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }

        // Возвращаем готовый объект.
        return retrofit;
    }

    // Создаём реализацию нашего интерфейса CatApiService.
    public static CatApiService getApiService() {

        return getClient().create(CatApiService.class);
    }
}
