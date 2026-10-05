package ru.mirea.semina.catworld.data.storage;

import android.content.Context;

import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.semina.catworld.data.database.CatDatabase;
import ru.mirea.semina.catworld.data.database.dao.FavoriteCatDao;
import ru.mirea.semina.catworld.data.database.entity.FavoriteCatEntity;
import ru.mirea.semina.catworld.domain.models.CatBreed;

// Хранилище избранных пород через Room.
public class RoomCatStorage implements CatStorage {

    // DAO для работы с таблицей favorite_cats.
    private final FavoriteCatDao favoriteCatDao;

    public RoomCatStorage(Context context) {

        // Создаём локальную базу данных.
        CatDatabase database =
                Room.databaseBuilder(
                                context,
                                CatDatabase.class,
                                "catworld_database"
                        )
                        .allowMainThreadQueries()
                        .build();

        // Получаем DAO из базы данных.
        favoriteCatDao =
                database.favoriteCatDao();
    }

    @Override
    public boolean saveFavorite(CatBreed catBreed) {

        // Проверяем, есть ли порода уже в избранном.
        int count =
                favoriteCatDao.countById(
                        catBreed.getId()
                );

        // Если уже есть, повторно не добавляем.
        if (count > 0) {
            return false;
        }

        // Преобразуем CatBreed в Entity для Room.
        FavoriteCatEntity entity =
                new FavoriteCatEntity(
                        catBreed.getId(),
                        catBreed.getName(),
                        catBreed.getDescription()
                );

        // Сохраняем запись в базу.
        favoriteCatDao.insert(entity);

        return true;
    }

    @Override
    public List<CatBreed> getFavorites() {

        // Получаем записи из базы данных.
        List<FavoriteCatEntity> entities =
                favoriteCatDao.getAll();

        // Создаём список domain-моделей.
        List<CatBreed> result =
                new ArrayList<>();

        // Преобразуем Entity в CatBreed.
        for (FavoriteCatEntity entity : entities) {

            result.add(
                    new CatBreed(
                            entity.getId(),
                            entity.getName(),
                            entity.getDescription()
                    )
            );
        }

        return result;
    }

    @Override
    public boolean deleteFavorite(int id) {

        // Проверяем наличие записи.
        int count =
                favoriteCatDao.countById(id);

        // Если записи нет, возвращаем false.
        if (count == 0) {
            return false;
        }

        // Удаляем породу из базы.
        favoriteCatDao.deleteById(id);

        return true;
    }
}