package ru.mirea.semina.catworld.data.repository;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.semina.catworld.data.storage.CatStorage;
import ru.mirea.semina.catworld.domain.models.CatBreed;
import ru.mirea.semina.catworld.domain.repository.CatRepository;

// Реализация репозитория.
public class CatRepositoryImpl implements CatRepository {

    // Хранилище избранных пород.
    private final CatStorage catStorage;

    // Учебный список пород.
    private final List<CatBreed> catBreeds =
            new ArrayList<>();

    public CatRepositoryImpl(CatStorage catStorage) {

        this.catStorage = catStorage;

        // Добавляем несколько пород.
        catBreeds.add(
                new CatBreed(
                        1,
                        "Мейн-кун",
                        "Крупная и дружелюбная порода кошек."
                )
        );

        catBreeds.add(
                new CatBreed(
                        2,
                        "Сиамская кошка",
                        "Активная и общительная порода."
                )
        );

        catBreeds.add(
                new CatBreed(
                        3,
                        "Британская короткошёрстная",
                        "Спокойная порода с плотной шерстью."
                )
        );

        catBreeds.add(
                new CatBreed(
                        4,
                        "Сфинкс",
                        "Порода кошек практически без шерсти."
                )
        );

        catBreeds.add(
                new CatBreed(
                        5,
                        "Бенгальская кошка",
                        "Активная порода с характерным пятнистым окрасом."
                )
        );
    }

    @Override
    public List<CatBreed> getCatBreeds() {

        return new ArrayList<>(catBreeds);
    }

    @Override
    public List<CatBreed> searchCatBreeds(String query) {

        List<CatBreed> result =
                new ArrayList<>();

        for (CatBreed breed : catBreeds) {

            if (breed
                    .getName()
                    .toLowerCase()
                    .contains(query.toLowerCase())) {

                result.add(breed);
            }
        }

        return result;
    }

    @Override
    public CatBreed getCatDetails(int id) {

        for (CatBreed breed : catBreeds) {

            if (breed.getId() == id) {
                return breed;
            }
        }

        return null;
    }

    @Override
    public boolean saveFavorite(CatBreed catBreed) {

        return catStorage.saveFavorite(catBreed);
    }

    @Override
    public List<CatBreed> getFavorites() {

        return catStorage.getFavorites();
    }

    @Override
    public boolean deleteFavorite(int id) {

        return catStorage.deleteFavorite(id);
    }

    @Override
    public CatBreed recognizeCatBreed(String imageName) {

        // Пока это учебная заглушка.
        // Для проверки всегда считаем,
        // что распознана Бенгальская кошка.
        return getCatDetails(5);
    }
}