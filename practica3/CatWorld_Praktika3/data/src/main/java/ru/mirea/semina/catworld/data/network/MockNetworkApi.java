package ru.mirea.semina.catworld.data.network;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.semina.catworld.domain.models.CatBreed;

// Тестовая реализация NetworkApi с замоканными данными.
public class MockNetworkApi implements NetworkApi {

    // Список пород, который имитирует ответ сервера.
    private final List<CatBreed> catBreeds =
            new ArrayList<>();

    public MockNetworkApi() {

        // Добавляем тестовые данные, как будто они пришли из сети.
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

        // Возвращаем копию списка пород.
        return new ArrayList<>(catBreeds);
    }

    @Override
    public CatBreed getCatBreedById(int id) {

        // Ищем породу по id.
        for (CatBreed breed : catBreeds) {

            if (breed.getId() == id) {
                return breed;
            }
        }

        // Если порода не найдена.
        return null;
    }
}