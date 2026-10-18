
package ru.mirea.semina.catworld;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.semina.catworld.domain.models.CatBreed;

// Адаптер отвечает за отображение пород кошек
// Каждая порода отображается отдельной карточкой
public class CatBreedAdapter
        extends RecyclerView.Adapter<CatBreedAdapter.BreedHolder> {

    // Список пород, полученных из ViewModel
    private final List<CatBreed> breeds = new ArrayList<>();

    // Обновляем список, когда LiveData сообщает об изменениях
    public void setItems(List<CatBreed> newItems) {

        // Удаляем старые данные из адаптера
        breeds.clear();

        // Добавляем новые породы
        if (newItems != null) {
            breeds.addAll(newItems);
        }

        // Сообщаем RecyclerView, что данные изменились
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BreedHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        // Создаём карточку из XML-разметки
        View view = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.item_cat_breed,
                        parent,
                        false
                );

        // Передаём карточку в ViewHolder
        return new BreedHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull BreedHolder holder,
            int position) {

        // Получаем породу по позиции в списке
        CatBreed breed = breeds.get(position);

        // Устанавливаем название породы
        holder.name.setText(breed.getName());

        // Устанавливаем описание породы
        holder.description.setText(breed.getDescription());

        // Определяем фотографию по ID породы
        int imageId;

        switch (breed.getId()) {

            case 1:
                // Мейн-кун
                imageId = R.drawable.maine_coon;
                break;

            case 2:
                // Сиамская кошка
                imageId = R.drawable.siamese;
                break;

            case 3:
                // Британская короткошёрстная
                imageId = R.drawable.british;
                break;

            case 4:
                // Сфинкс
                imageId = R.drawable.sphynx;
                break;

            case 5:
                // Бенгальская кошка
                imageId = R.drawable.bengal;
                break;

            default:
                // Запасное изображение
                imageId = R.drawable.bengal;
                break;
        }

        // Показываем фотографию в карточке
        holder.image.setImageResource(imageId);

        // Описание для доступности
        holder.image.setContentDescription(
                "Фотография породы " + breed.getName()
        );
    }

    @Override
    public int getItemCount() {

        // Возвращаем количество карточек
        return breeds.size();
    }

    // ViewHolder хранит элементы одной карточки
    static class BreedHolder extends RecyclerView.ViewHolder {

        final ImageView image;
        final TextView name;
        final TextView description;

        BreedHolder(@NonNull View itemView) {
            super(itemView);

            // Находим фотографию
            image = itemView.findViewById(R.id.breedImage);

            // Находим название породы
            name = itemView.findViewById(R.id.breedName);

            // Находим описание породы
            description = itemView.findViewById(
                    R.id.breedDescription
            );
        }
    }
}
