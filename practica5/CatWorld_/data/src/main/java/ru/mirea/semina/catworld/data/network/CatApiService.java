package ru.mirea.semina.catworld.data.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

// Описываем запрос к The Cat API.
public interface CatApiService {

    // Получаем список пород кошек.
    @GET("v1/breeds")
    Call<List<CatApiBreedDto>> getBreeds(

            // Передаём API-ключ в заголовке запроса.
            @Header("x-api-key") String apiKey
    );
}