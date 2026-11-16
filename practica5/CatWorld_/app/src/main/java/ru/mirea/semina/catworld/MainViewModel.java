
package ru.mirea.semina.catworld;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.net.UnknownHostException;
import java.net.SocketTimeoutException;
import java.util.Collections;
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

// ViewModel хранит данные экрана и вызывает UseCase из domain.
// Она не обращается напрямую к Retrofit или Room.
public class MainViewModel extends ViewModel {

    // Сценарии работы приложения.
    private final GetCatBreedsUseCase getBreeds;
    private final SearchCatBreedsUseCase searchBreeds;
    private final GetCatDetailsUseCase getDetails;
    private final SaveFavoriteUseCase saveFavorite;
    private final GetFavoritesUseCase getFavorites;
    private final DeleteFavoriteUseCase deleteFavorite;
    private final RecognizeCatBreedUseCase recognizeBreed;
    private final LogoutUserUseCase logoutUser;

    // LiveData со списком пород для RecyclerView.
    private final MutableLiveData<List<CatBreed>> breedItems =
            new MutableLiveData<>();

    // НОВОЕ: состояние загрузки списка пород.
    // Здесь будут сообщения о загрузке и ошибках.
    private final MutableLiveData<String> breedLoadStatus =
            new MutableLiveData<>("");

    // Текст результата для главного экрана.
    private final MutableLiveData<String> result =
            new MutableLiveData<>("Результат");

    // Первый источник данных: породы из репозитория.
    private final MutableLiveData<List<CatBreed>> networkBreeds =
            new MutableLiveData<>();

    // Второй источник данных: избранное из Room.
    private final MutableLiveData<List<CatBreed>> databaseFavorites =
            new MutableLiveData<>();

    // MediatorLiveData объединяет данные из двух источников.
    private final MediatorLiveData<String> combinedData =
            new MediatorLiveData<>();

    // Фоновый поток для работы с сетью и базой данных.
    private final ExecutorService executor =
            Executors.newSingleThreadExecutor();

    // true означает, что приложение открыто гостем.
    private boolean guest;

    // Фабрика передаёт сюда готовые репозитории.
    public MainViewModel(
            CatRepository repository,
            AuthRepository authRepository
    ) {
        // Создаём UseCase на основе репозиториев.
        getBreeds = new GetCatBreedsUseCase(repository);
        searchBreeds = new SearchCatBreedsUseCase(repository);
        getDetails = new GetCatDetailsUseCase(repository);
        saveFavorite = new SaveFavoriteUseCase(repository);
        getFavorites = new GetFavoritesUseCase(repository);
        deleteFavorite = new DeleteFavoriteUseCase(repository);
        recognizeBreed = new RecognizeCatBreedUseCase(repository);
        logoutUser = new LogoutUserUseCase(authRepository);

        // Если изменился список пород, обновляем сводку.
        combinedData.addSource(
                networkBreeds,
                breeds -> updateCombined()
        );

        // Если изменилось избранное, тоже обновляем сводку.
        combinedData.addSource(
                databaseFavorites,
                favorites -> updateCombined()
        );

        Log.d("CatWorldMVVM", "MainViewModel created");
    }

    // Activity получает LiveData, но не может менять её напрямую.
    public LiveData<List<CatBreed>> getBreedItems() {
        return breedItems;
    }

    // НОВОЕ: Activity сможет показывать состояние загрузки.
    public LiveData<String> getBreedLoadStatus() {
        return breedLoadStatus;
    }

    // Результаты операций для главного экрана.
    public LiveData<String> getResult() {
        return result;
    }

    // Общая информация о сети и базе данных.
    public LiveData<String> getCombinedData() {
        return combinedData;
    }

    // Сохраняем информацию о гостевом режиме.
    public void setGuest(boolean guest) {
        this.guest = guest;
    }

    // НОВОЕ: загрузка пород с обработкой ошибок.
    public void loadBreedItems() {

        // Показываем, что загрузка началась.
        breedLoadStatus.setValue("Загружаем породы кошек...");

        // Выполняем запрос не в главном потоке.
        executor.execute(() -> {
            try {
                // UseCase обращается к репозиторию.
                // Репозиторий уже выбирает источник данных.
                List<CatBreed> breeds = getBreeds.execute();

                // Если репозиторий вернул пустой результат.
                if (breeds == null || breeds.isEmpty()) {

                    breedItems.postValue(Collections.emptyList());

                    breedLoadStatus.postValue(
                            "Породы не найдены"
                    );

                    return;
                }

                // Передаём полученные породы в LiveData.
                // RecyclerView обновится через наблюдатель.
                breedItems.postValue(breeds);

                // Пустая строка означает успешную загрузку.
                breedLoadStatus.postValue("");

                // Сохраняем список и для общей сводки.
                networkBreeds.postValue(breeds);

            } catch (Exception e) {

                // Записываем техническую ошибку в Logcat.
                Log.e(
                        "CatWorldMVVM",
                        "Ошибка загрузки пород",
                        e
                );

                // Очищаем список, чтобы не показывать старые данные.
                breedItems.postValue(Collections.emptyList());

                // Показываем пользователю понятное сообщение.
                breedLoadStatus.postValue(getErrorMessage(e));
            }
        });
    }

    // НОВОЕ: преобразуем сетевые ошибки в понятный текст.
    private String getErrorMessage(Exception e) {

        // Сервер недоступен или отсутствует интернет.
        if (e instanceof UnknownHostException) {
            return "Нет подключения к интернету. "
                    + "Проверьте сеть и попробуйте снова.";
        }

        // Сервер слишком долго не отвечает.
        if (e instanceof SocketTimeoutException) {
            return "Сервер долго не отвечает. "
                    + "Попробуйте загрузить список позже.";
        }

        // Другие ошибки передачи данных.
        if (e instanceof IOException) {
            return "Ошибка соединения с сервером. "
                    + "Попробуйте ещё раз.";
        }

        // Остальные ошибки, например некорректный ответ сервера.
        return "Не удалось загрузить данные. "
                + "Попробуйте ещё раз.";
    }

    // Общий метод выполнения операций в фоновом потоке.
    private void work(Runnable action) {

        executor.execute(() -> {
            try {
                action.run();

            } catch (Exception e) {

                // Подробности ошибки остаются в Logcat.
                Log.e("CatWorldMVVM", "Data error", e);

                // Пользователь видит короткое сообщение.
                result.postValue(getErrorMessage(e));
            }
        });
    }

    // Показать названия всех пород на главном экране.
    public void showBreeds() {

        work(() -> {

            List<CatBreed> breeds = getBreeds.execute();

            result.postValue(
                    names(breeds, "Пород пока нет")
            );

            // Обновляем данные первого источника.
            networkBreeds.postValue(breeds);
        });
    }

    // Поиск породы по названию.
    public void search(String query) {

        // Проверяем, что пользователь ввёл название.
        if (query == null || query.trim().isEmpty()) {
            result.setValue("Введите название породы");
            return;
        }

        work(() -> {

            List<CatBreed> breeds =
                    searchBreeds.execute(query.trim());

            result.postValue(
                    names(breeds, "Порода не найдена")
            );
        });
    }

    // Показываем информацию о бенгальской кошке.
    public void showDetails() {

        work(() -> {

            CatBreed breed = getDetails.execute(5);

            if (breed == null) {
                result.postValue("Порода не найдена");
                return;
            }

            result.postValue(
                    "Порода: " + breed.getName()
                            + "\n\nОписание: "
                            + breed.getDescription()
            );
        });
    }

    // Добавление бенгальской кошки в избранное.
    public void addFavorite() {

        // Гостю запрещено изменять избранное.
        if (guest) {
            result.setValue(
                    "Для добавления в избранное войдите в аккаунт"
            );
            return;
        }

        work(() -> {

            CatBreed breed = getDetails.execute(5);

            if (breed == null) {
                result.postValue(
                        "Бенгальская кошка не найдена"
                );
                return;
            }

            // Сохраняем породу через UseCase.
            boolean saved = saveFavorite.execute(breed);

            result.postValue(
                    saved
                            ? "Бенгальская кошка добавлена в избранное"
                            : "Эта порода уже находится в избранном"
            );

            // Обновляем список из Room.
            databaseFavorites.postValue(
                    getFavorites.execute()
            );
        });
    }

    // Показать все избранные породы.
    public void showFavorites() {

        if (guest) {
            result.setValue(
                    "Для просмотра избранного войдите в аккаунт"
            );
            return;
        }

        work(() -> {

            List<CatBreed> breeds = getFavorites.execute();

            result.postValue(
                    breeds == null || breeds.isEmpty()
                            ? "Избранное пусто"
                            : "Избранное:\n" + names(breeds, "")
            );

            databaseFavorites.postValue(breeds);
        });
    }

    // Удалить бенгальскую кошку из избранного.
    public void removeFavorite() {

        if (guest) {
            result.setValue(
                    "Для изменения избранного войдите в аккаунт"
            );
            return;
        }

        work(() -> {

            boolean deleted = deleteFavorite.execute(5);

            result.postValue(
                    deleted
                            ? "Бенгальская кошка удалена из избранного"
                            : "Бенгальской кошки нет в избранном"
            );

            // Получаем обновлённые данные из Room.
            databaseFavorites.postValue(
                    getFavorites.execute()
            );
        });
    }

    // Распознавание породы.
    public void recognize() {

        work(() -> {

            // Пока это учебная заглушка.
            // Реальная модель TensorFlow Lite ещё не подключена.
            CatBreed breed =
                    recognizeBreed.execute("cat_photo.jpg");

            result.postValue(
                    breed == null
                            ? "Не удалось распознать породу"
                            : "Распознанная порода: "
                              + breed.getName()
            );
        });
    }

    // Обновление информации о сети и Room.
    public void refreshCombinedData() {

        work(() -> {

            // Получаем породы через репозиторий.
            networkBreeds.postValue(
                    getBreeds.execute()
            );

            // Получаем избранное из локальной базы.
            databaseFavorites.postValue(
                    getFavorites.execute()
            );
        });
    }

    // Собираем общую информацию из двух LiveData.
    private void updateCombined() {

        List<CatBreed> network =
                networkBreeds.getValue();

        List<CatBreed> local =
                databaseFavorites.getValue();

        // Ждём, пока оба источника вернут данные.
        if (network == null || local == null) {

            combinedData.setValue(
                    "Загружаем данные сети и базы..."
            );

            return;
        }

        // Выводим количество пород и избранных записей.
        combinedData.setValue(
                "Источник пород: " + network.size() + " пород\n"
                        + "Room: " + local.size()
                        + " в избранном\n\n"
                        + "Породы: "
                        + names(network, "Нет данных")
                        + "\nИзбранное из БД: "
                        + names(local, "пусто")
        );
    }

    // Преобразуем список пород в обычный текст.
    private String names(
            List<CatBreed> breeds,
            String emptyText
    ) {

        if (breeds == null || breeds.isEmpty()) {
            return emptyText;
        }

        StringBuilder text = new StringBuilder();

        // Добавляем название каждой породы.
        for (CatBreed breed : breeds) {
            text.append(breed.getName()).append("\n");
        }

        return text.toString();
    }

    // Выход из аккаунта.
    public void logout() {

        if (!guest) {
            logoutUser.execute();
        }
    }

    // Освобождаем ресурсы при уничтожении ViewModel.
    @Override
    protected void onCleared() {

        // Завершаем фоновый поток.
        executor.shutdown();

        Log.d(
                "CatWorldMVVM",
                "MainViewModel cleared"
        );

        super.onCleared();
    }
}
