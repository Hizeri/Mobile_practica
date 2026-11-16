
package ru.mirea.semina.catworld;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.semina.catworld.domain.models.CatBreed;

// Адаптер отображает карточки пород в RecyclerView.
public class CatBreedAdapter
        extends RecyclerView.Adapter<CatBreedAdapter.BreedHolder> {

    // Список пород, полученных из ViewModel.
    private final List<CatBreed> breeds = new ArrayList<>();

    // слушатель нажатий на карточки.
    private final OnBreedClickListener clickListener;

    // Интерфейс сообщает, какую породу выбрал пользователь.
    public interface OnBreedClickListener {
        void onBreedClick(CatBreed breed);
    }

    public CatBreedAdapter() {
        this.clickListener = null;
    }

    // конструктор с обработчиком нажатий на карточку
    public CatBreedAdapter(OnBreedClickListener clickListener) {
        this.clickListener = clickListener;
    }

    // Обновляем список пород.
    public void setItems(List<CatBreed> newItems) {

        breeds.clear();

        if (newItems != null) {
            breeds.addAll(newItems);
        }

        // Сообщаем RecyclerView об изменении данных.
        notifyDataSetChanged();
    }

    // Создаём разметку карточки.
    @NonNull
    @Override
    public BreedHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(
                        R.layout.item_cat_breed,
                        parent,
                        false
                );

        return new BreedHolder(view);
    }

    // Заполняем карточку информацией о породе.
    @Override
    public void onBindViewHolder(
            @NonNull BreedHolder holder,
            int position
    ) {

        // Получаем выбранную породу.
        CatBreed breed = breeds.get(position);

        // Отображаем название.
        holder.name.setText(breed.getName());

        // Отображаем описание.
        holder.description.setText(breed.getDescription());

        // Определяем запасную фотографию.
        int fallbackImage = getFallbackImage(breed.getId());

        // Получаем ссылку на фото из The Cat API.
        String imageUrl = breed.getImageUrl();

        // Настраиваем отображение фотографии.
        holder.image.setScaleType(
                ImageView.ScaleType.CENTER_CROP
        );

        if (imageUrl != null && !imageUrl.trim().isEmpty()) {

            // Загружаем фотографию через Picasso.
            Picasso.get()
                    .load(imageUrl.trim())
                    .noPlaceholder()
                    .into(holder.image);

        } else {

            // Если ссылки нет, используем локальное фото.
            Picasso.get().cancelRequest(holder.image);
            holder.image.setImageResource(fallbackImage);
        }

        // Описание изображения для доступности.
        holder.image.setContentDescription(
                "Фотография породы " + breed.getName()
        );

        // обрабатываем нажатие на карточку.
        holder.itemView.setOnClickListener(v -> {

            // Сообщаем Activity, какую породу нажали.
            if (clickListener != null) {
                clickListener.onBreedClick(breed);
            }
        });
    }

    // Выбираем запасную фотографию по ID породы.
    private int getFallbackImage(int breedId) {

        switch (breedId) {

            case 1:
                return R.drawable.maine_coon;

            case 2:
                return R.drawable.siamese;

            case 3:
                return R.drawable.british;

            case 4:
                return R.drawable.sphynx;

            case 5:
                return R.drawable.bengal;

            default:
                return R.drawable.bengal;
        }
    }

    // Количество карточек.
    @Override
    public int getItemCount() {
        return breeds.size();
    }

    // ViewHolder хранит элементы карточки.
    static class BreedHolder extends RecyclerView.ViewHolder {

        final ImageView image;
        final TextView name;
        final TextView description;

        BreedHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(
                    R.id.breedImage
            );

            name = itemView.findViewById(
                    R.id.breedName
            );

            description = itemView.findViewById(
                    R.id.breedDescription
            );
        }
    }
}
