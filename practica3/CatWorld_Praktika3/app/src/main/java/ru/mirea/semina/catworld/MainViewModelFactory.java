package ru.mirea.semina.catworld;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.semina.catworld.data.auth.FirebaseAuthRepository;
import ru.mirea.semina.catworld.data.network.MockNetworkApi;
import ru.mirea.semina.catworld.data.repository.CatRepositoryImpl;
import ru.mirea.semina.catworld.data.storage.RoomCatStorage;
import ru.mirea.semina.catworld.domain.repository.CatRepository;

// Фабрика создаёт зависимости, не передавая Android Context внутрь ViewModel.
public class MainViewModelFactory implements ViewModelProvider.Factory {
    private final Context applicationContext;

    public MainViewModelFactory(Context context) {
        this.applicationContext = context.getApplicationContext();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (!modelClass.isAssignableFrom(MainViewModel.class)) {
            throw new IllegalArgumentException("Unknown ViewModel: " + modelClass);
        }
        // Repository связывает mock-сеть и базу данных Room.
        CatRepository repository = new CatRepositoryImpl(
                new MockNetworkApi(), new RoomCatStorage(applicationContext));
        return modelClass.cast(new MainViewModel(repository, new FirebaseAuthRepository()));
    }
}
