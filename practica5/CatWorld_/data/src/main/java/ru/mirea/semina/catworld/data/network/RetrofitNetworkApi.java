
package ru.mirea.semina.catworld.data.network;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

import ru.mirea.semina.catworld.domain.models.CatBreed;

// Получает породы кошек через Retrofit.
public class RetrofitNetworkApi implements NetworkApi {

    // Название для сообщений в Logcat.
    private static final String TAG = "CatWorldAPI";

    // Сервис для запросов к The Cat API.
    private final CatApiService apiService;

    // Ключ доступа к серверу.
    private final String apiKey;

    // Список пород, сохранённый в памяти.
    private List<CatBreed> cachedBreeds;

    // Конструктор принимает API-ключ.
    public RetrofitNetworkApi(String apiKey) {

        // Убираем случайные пробелы.
        this.apiKey = apiKey == null ? "" : apiKey.trim();

        // Получаем настроенный Retrofit-сервис.
        this.apiService = RetrofitClient.getApiService();
    }

    // Получаем список пород из интернета.
    @Override
    public synchronized List<CatBreed> getCatBreeds() {

        // Если список уже загружен, используем его.
        if (cachedBreeds != null) {
            return new ArrayList<>(cachedBreeds);
        }

        try {

            // Отправляем GET-запрос к серверу.
            Response<List<CatApiBreedDto>> response = apiService.getBreeds(apiKey).execute();

            // Проверяем ответ сервера.
            if (!response.isSuccessful()) {

                int errorCode = response.code();

                // Записываем ошибку в Logcat.
                Log.e(TAG, "Ошибка сервера: " + errorCode);

                throw new IllegalStateException(
                        "Ошибка сервера: " + errorCode
                );
            }

            // Получаем список пород из JSON.
            List<CatApiBreedDto> serverBreeds = response.body();

            // Проверяем наличие данных.
            if (serverBreeds == null) {
                throw new IllegalStateException("Сервер вернул пустой ответ");
            }

            // Создаём список пород CatWorld.
            List<CatBreed> result = new ArrayList<>();

            // Перебираем все породы с сервера.
            for (CatApiBreedDto dto : serverBreeds) {

                // Пропускаем пустые записи.
                if (dto == null) {
                    continue;
                }

                // Преобразуем ID сервера в ID CatWorld.
                int localId = getLocalId(dto.getId());

                // Оставляем пять пород нашего приложения.
                if (localId == 0) {
                    continue;
                }

                // Получаем русское название породы.
                String russianName =
                        getRussianName(localId);

                // Получаем русское описание породы.
                String russianDescription =
                        getRussianDescription(localId);

                // получаем ссылку на фотографию.
                String imageUrl = dto.getImageUrl();

                // Если ссылка есть, она будет передана
                if (imageUrl != null && !imageUrl.isEmpty()) {

                    Log.d(TAG, "Получена ссылка на фото: " + russianName);

                } else {

                    // Если ссылки нет, адаптер покажет
                    // запасную фотографию из drawable.
                    Log.d(TAG, "Нет ссылки на фото: " + russianName);
                }

                //  создаём модель с четырьмя полями.
                result.add(new CatBreed(localId, russianName, russianDescription, imageUrl));
            }

            // Проверяем, что нужные породы найдены.
            if (result.isEmpty()) {
                throw new IllegalStateException("Не удалось получить породы кошек");
            }

            // Сортируем породы по ID от 1 до 5.
            result.sort(
                    (a, b) -> Integer.compare(
                            a.getId(),
                            b.getId()
                    )
            );

            // Сохраняем список в памяти.
            cachedBreeds = new ArrayList<>(result);

            // Выводим результат в Logcat.
            Log.d(
                    TAG,
                    "Загружено пород: " + cachedBreeds.size()
            );

            // Возвращаем копию списка.
            return new ArrayList<>(cachedBreeds);

        } catch (IOException e) {

            // Ошибка подключения к серверу.
            Log.e(TAG, "Ошибка соединения", e);

            throw new IllegalStateException(
                    "Не удалось подключиться к интернету",
                    e
            );
        }
    }

    // Получаем одну породу по её ID.
    @Override
    public CatBreed getCatBreedById(int id) {

        for (CatBreed breed : getCatBreeds()) {

            if (breed.getId() == id) {
                return breed;
            }
        }

        return null;
    }

    // Сопоставляем ID The Cat API с ID CatWorld.
    private int getLocalId(String serverId) {

        if (serverId == null) {
            return 0;
        }

        switch (serverId) {

            case "mcoo":
                return 1; // Мейн-кун

            case "siam":
                return 2; // Сиамская кошка

            case "bsho":
                return 3; // Британская короткошёрстная

            case "sphy":
                return 4; // Сфинкс

            case "beng":
                return 5; // Бенгальская кошка

            default:
                return 0;
        }
    }

    // Русские названия пород.
    private String getRussianName(int id) {

        switch (id) {

            case 1:
                return "Мейн-кун";

            case 2:
                return "Сиамская кошка";

            case 3:
                return "Британская короткошёрстная";

            case 4:
                return "Сфинкс";

            case 5:
                return "Бенгальская кошка";

            default:
                return "Неизвестная порода";
        }
    }

    // Русские описания пяти пород CatWorld.
    private String getRussianDescription(int id) {

        switch (id) {

            case 1:
                return "Мейн-кун - крупная домашняя кошка "
                        + "с длинной густой шерстью и пушистым "
                        + "хвостом. Отличается дружелюбным "
                        + "и спокойным характером.";

            case 2:
                return "Сиамская кошка - стройная и изящная "
                        + "порода с голубыми глазами и короткой "
                        + "шерстью. Очень общительная, "
                        + "активная и любознательная.";

            case 3:
                return "Британская короткошёрстная - кошка "
                        + "с густой плюшевой шерстью, "
                        + "круглой мордочкой и спокойным "
                        + "характером.";

            case 4:
                return "Сфинкс - необычная кошка практически "
                        + "без шерсти, с большими ушами "
                        + "и выразительными глазами. "
                        + "Любит внимание и общение.";

            case 5:
                return "Бенгальская кошка - активная "
                        + "и энергичная порода с красивым "
                        + "пятнистым окрасом, напоминающим "
                        + "окрас дикого леопарда.";

            default:
                return "Описание породы отсутствует";
        }
    }
}
