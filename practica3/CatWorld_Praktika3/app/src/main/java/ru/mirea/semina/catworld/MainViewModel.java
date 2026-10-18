package ru.mirea.semina.catworld;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.mirea.semina.catworld.domain.auth.AuthRepository;
import ru.mirea.semina.catworld.domain.auth.usecases.LogoutUserUseCase;
import ru.mirea.semina.catworld.domain.models.CatBreed;
import ru.mirea.semina.catworld.domain.repository.CatRepository;
import ru.mirea.semina.catworld.domain.usecases.DeleteFavoriteUseCase;
import ru.mirea.semina.catworld.domain.usecases.GetCatBreedsUseCase;
import ru.mirea.semina.catworld.domain.usecases.GetCatDetailsUseCase;
import ru.mirea.semina.catworld.domain.usecases.GetFavoritesUseCase;
import ru.mirea.semina.catworld.domain.usecases.RecognizeCatBreedUseCase;
import ru.mirea.semina.catworld.domain.usecases.SaveFavoriteUseCase;
import ru.mirea.semina.catworld.domain.usecases.SearchCatBreedsUseCase;

// ViewModel хранит состояние экрана и обращается к domain через UseCase.
public class MainViewModel extends ViewModel {
    private final GetCatBreedsUseCase getBreeds;
    private final SearchCatBreedsUseCase searchBreeds;
    private final GetCatDetailsUseCase getDetails;
    private final SaveFavoriteUseCase saveFavorite;
    private final GetFavoritesUseCase getFavorites;
    private final DeleteFavoriteUseCase deleteFavorite;
    private final RecognizeCatBreedUseCase recognizeBreed;
    private final LogoutUserUseCase logoutUser;

    // Обычная LiveData для текста результата.
    private final MutableLiveData<String> result = new MutableLiveData<>("Результат");
    private final MutableLiveData<List<CatBreed>> networkBreeds = new MutableLiveData<>();
    private final MutableLiveData<List<CatBreed>> databaseFavorites = new MutableLiveData<>();

    // MediatorLiveData объединяет два источника: mock-сеть и Room.
    private final MediatorLiveData<String> combinedData = new MediatorLiveData<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private boolean guest;

    public MainViewModel(CatRepository repository, AuthRepository authRepository) {
        getBreeds = new GetCatBreedsUseCase(repository);
        searchBreeds = new SearchCatBreedsUseCase(repository);
        getDetails = new GetCatDetailsUseCase(repository);
        saveFavorite = new SaveFavoriteUseCase(repository);
        getFavorites = new GetFavoritesUseCase(repository);
        deleteFavorite = new DeleteFavoriteUseCase(repository);
        recognizeBreed = new RecognizeCatBreedUseCase(repository);
        logoutUser = new LogoutUserUseCase(authRepository);

        // При изменении любого источника пересобираем сводку.
        combinedData.addSource(networkBreeds, breeds -> updateCombined());
        combinedData.addSource(databaseFavorites, favorites -> updateCombined());
        Log.d("CatWorldMVVM", "MainViewModel created");
    }

    public LiveData<String> getResult() { return result; }
    public LiveData<String> getCombinedData() { return combinedData; }
    public void setGuest(boolean guest) { this.guest = guest; }

    // Для Room используем фоновый поток, чтобы не блокировать экран.
    private void work(Runnable action) {
        executor.execute(() -> {
            try { action.run(); }
            catch (Exception e) {
                Log.e("CatWorldMVVM", "Data error", e);
                result.postValue("Ошибка: " + e.getMessage());
            }
        });
    }

    public void showBreeds() {
        work(() -> {
            List<CatBreed> breeds = getBreeds.execute();
            result.postValue(names(breeds, "Пород пока нет"));
            networkBreeds.postValue(breeds);
        });
    }

    public void search(String query) {
        if (query.trim().isEmpty()) { result.setValue("Введите название породы"); return; }
        work(() -> result.postValue(names(searchBreeds.execute(query), "Порода не найдена")));
    }

    public void showDetails() {
        work(() -> {
            CatBreed breed = getDetails.execute(5);
            result.postValue(breed == null ? "Порода не найдена" :
                    "Порода: " + breed.getName() + "\n\nОписание: " + breed.getDescription());
        });
    }

    public void addFavorite() {
        if (guest) return;
        work(() -> {
            CatBreed breed = getDetails.execute(5);
            if (breed == null) { result.postValue("Бенгальская кошка не найдена"); return; }
            boolean saved = saveFavorite.execute(breed);
            result.postValue(saved ? "Бенгальская кошка добавлена в избранное" :
                    "Эта порода уже находится в избранном");
            databaseFavorites.postValue(getFavorites.execute());
        });
    }

    public void showFavorites() {
        if (guest) return;
        work(() -> {
            List<CatBreed> breeds = getFavorites.execute();
            result.postValue(breeds.isEmpty() ? "Избранное пусто" : "Избранное:\n" + names(breeds, ""));
            databaseFavorites.postValue(breeds);
        });
    }

    public void removeFavorite() {
        if (guest) return;
        work(() -> {
            boolean deleted = deleteFavorite.execute(5);
            result.postValue(deleted ? "Бенгальская кошка удалена из избранного" :
                    "Бенгальской кошки нет в избранном");
            databaseFavorites.postValue(getFavorites.execute());
        });
    }

    public void recognize() {
        work(() -> {
            // Распознавание пока остаётся заглушкой, как в исходном проекте.
            CatBreed breed = recognizeBreed.execute("cat_photo.jpg");
            result.postValue(breed == null ? "Не удалось распознать породу" :
                    "Распознанная порода: " + breed.getName());
        });
    }

    // Загружаем отдельно данные mock-сети и локальной БД.
    public void refreshCombinedData() {
        work(() -> {
            networkBreeds.postValue(getBreeds.execute());
            databaseFavorites.postValue(getFavorites.execute());
        });
    }

    private void updateCombined() {
        List<CatBreed> network = networkBreeds.getValue();
        List<CatBreed> local = databaseFavorites.getValue();
        if (network == null || local == null) {
            combinedData.setValue("Загружаем данные сети и базы...");
            return;
        }
        combinedData.setValue("MockNetworkApi: " + network.size() + " пород\n" +
                "Room: " + local.size() + " в избранном\n\n" +
                "Сеть: " + names(network, "Нет данных") +
                "\nИзбранное из БД: " + names(local, "пусто"));
    }

    private String names(List<CatBreed> breeds, String emptyText) {
        if (breeds == null || breeds.isEmpty()) return emptyText;
        StringBuilder text = new StringBuilder();
        for (CatBreed breed : breeds) text.append(breed.getName()).append("\n");
        return text.toString();
    }

    public void logout() {
        if (!guest) logoutUser.execute();
    }

    @Override
    protected void onCleared() {
        // ViewModel живёт при повороте экрана, а при окончательном закрытии очищается.
        executor.shutdown();
        Log.d("CatWorldMVVM", "MainViewModel cleared");
        super.onCleared();
    }
}
