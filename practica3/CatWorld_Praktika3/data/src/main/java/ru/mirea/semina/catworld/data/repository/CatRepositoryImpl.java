package ru.mirea.semina.catworld.data.repository;

import java.util.List;

import ru.mirea.semina.catworld.data.network.NetworkApi;
import ru.mirea.semina.catworld.data.storage.CatStorage;
import ru.mirea.semina.catworld.domain.models.CatBreed;
import ru.mirea.semina.catworld.domain.repository.CatRepository;

// Реализация репозитория для работы с породами кошек.
public class CatRepositoryImpl implements CatRepository {

    // Сетевой источник данных о породах.
    private final NetworkApi networkApi;

    // Локальное хранилище избранных пород.
    private final CatStorage catStorage;

    // Передаём в репозиторий сеть и локальное хранилище.
    public CatRepositoryImpl(
            NetworkApi networkApi,
            CatStorage catStorage
    ) {
        this.networkApi = networkApi;
        this.catStorage = catStorage;
    }

    @Override
    public List<CatBreed> getCatBreeds() {

        // Получаем список пород из NetworkApi.
        return networkApi.getCatBreeds();
    }

    @Override
    public List<CatBreed> searchCatBreeds(String query) {

        // Получаем все породы из NetworkApi.
        List<CatBreed> catBreeds =
                networkApi.getCatBreeds();

        // Создаём список для результатов поиска.
        java.util.ArrayList<CatBreed> result =
                new java.util.ArrayList<>();

        // Перебираем все породы.
        for (CatBreed breed : catBreeds) {

            // Сравниваем название с запросом пользователя.
            if (breed
                    .getName()
                    .toLowerCase()
                    .contains(query.toLowerCase())) {

                result.add(breed);
            }
        }

        // Возвращаем найденные породы.
        return result;
    }

    @Override
    public CatBreed getCatBreedById(int id) {

        // Получаем конкретную породу из NetworkApi.
        return networkApi.getCatBreedById(id);
    }

    @Override
    public boolean saveFavorite(CatBreed catBreed) {

        // Сохраняем породу в локальное хранилище.
        return catStorage.saveFavorite(catBreed);
    }

    @Override
    public List<CatBreed> getFavorites() {

        // Получаем избранные породы из локального хранилища.
        return catStorage.getFavorites();
    }

    @Override
    public boolean deleteFavorite(int id) {

        // Удаляем породу из локального хранилища.
        return catStorage.deleteFavorite(id);
    }

    @Override
    public CatBreed recognizeCatBreed(String imageName) {

        // Пока распознавание является учебной заглушкой.
        return networkApi.getCatBreedById(5);
    }
}