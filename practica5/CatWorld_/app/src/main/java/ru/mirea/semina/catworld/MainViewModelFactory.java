package ru.mirea.semina.catworld;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.semina.catworld.data.auth.FirebaseAuthRepository;
import ru.mirea.semina.catworld.data.network.RetrofitNetworkApi;
import ru.mirea.semina.catworld.data.repository.CatRepositoryImpl;
import ru.mirea.semina.catworld.data.storage.RoomCatStorage;
import ru.mirea.semina.catworld.domain.repository.CatRepository;

// Фабрика создаёт MainViewModel и необходимые ей объекты.
public class MainViewModelFactory implements ViewModelProvider.Factory {

    // Контекст приложения нужен для работы с базой Room.
    private final Context applicationContext;

    public MainViewModelFactory(Context context) {
        this.applicationContext = context.getApplicationContext();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(
            @NonNull Class<T> modelClass) {

        // Проверяем, что создаётся нужная ViewModel.
        if (!modelClass.isAssignableFrom(MainViewModel.class)) {
            throw new IllegalArgumentException(
                    "Unknown ViewModel: " + modelClass
            );
        }

        // Получаем API-ключ из ресурсов приложения.
// trim() удаляет случайные пробелы в начале и конце ключа.
        String apiKey = applicationContext
                .getString(R.string.cat_api_key)
                .trim();

// Создаём сетевой источник Retrofit и передаём ему ключ.
        RetrofitNetworkApi networkApi =
                new RetrofitNetworkApi(apiKey);

        // Репозиторий объединяет:
        // 1. Retrofit — данные из интернета.
        // 2. Room — избранные породы в телефоне.
        CatRepository repository = new CatRepositoryImpl(
                networkApi,
                new RoomCatStorage(applicationContext)
        );

        // Создаём ViewModel с готовыми зависимостями.
        return modelClass.cast(
                new MainViewModel(
                        repository,
                        new FirebaseAuthRepository()
                )
        );
    }
}
