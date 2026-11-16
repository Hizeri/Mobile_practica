package ru.mirea.semina.retrofitapp;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

// Описание HTTP-запросов. Retrofit сам создаёт реализацию интерфейса.
public interface ApiService {

    // GET https://jsonplaceholder.typicode.com/todos
    @GET("todos")
    Call<List<Todo>> getTodos();

    // PATCH https://jsonplaceholder.typicode.com/todos/1
    // @Path подставляет ID задачи, @Body преобразуется Gson в JSON.
    @PATCH("todos/{id}")
    Call<Todo> updateTodo(@Path("id") int id, @Body TodoUpdate update);
}
