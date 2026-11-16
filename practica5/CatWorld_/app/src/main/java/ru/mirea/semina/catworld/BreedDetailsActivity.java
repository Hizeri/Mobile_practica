
package ru.mirea.semina.catworld;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

// Экран подробной информации об одной породе.
public class BreedDetailsActivity extends AppCompatActivity {

    // Ключи для получения данных из Intent.
    public static final String EXTRA_BREED_ID = "breed_id";

    public static final String EXTRA_BREED_NAME = "breed_name";

    public static final String EXTRA_BREED_DESCRIPTION = "breed_description";

    public static final String EXTRA_BREED_IMAGE_URL = "breed_image_url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Подключаем разметку страницы породы.
        setContentView(R.layout.activity_breed_details);

        // Находим элементы интерфейса.
        ImageView breedImage = findViewById(R.id.detailsBreedImage);

        TextView breedName = findViewById(R.id.detailsBreedName);

        TextView breedDescription = findViewById(R.id.detailsBreedDescription);

        Button backButton = findViewById(R.id.detailsBackButton);

        // Получаем ID выбранной породы.
        int id = getIntent().getIntExtra(EXTRA_BREED_ID, 0);

        // Получаем название породы.
        String name = getIntent().getStringExtra(EXTRA_BREED_NAME);

        // Получаем описание породы.
        String description = getIntent().getStringExtra(EXTRA_BREED_DESCRIPTION);

        // Получаем ссылку на фотографию.
        String imageUrl = getIntent().getStringExtra(EXTRA_BREED_IMAGE_URL);

        // я от отсутствующих данных.
        if (name == null || name.isEmpty()) {
            name = "Неизвестная порода";
        }

        if (description == null || description.isEmpty()) {
            description = "Описание отсутствует";
        }

        // Показываем название и описание.
        breedName.setText(name);
        breedDescription.setText(description);

        // Описание изображения для доступности.
        breedImage.setContentDescription("Фотография породы " + name);

        // Определяем запасную фотографию.
        int fallbackImage = getFallbackImage(id);

        // Загружаем большое фото через Picasso.
        if (imageUrl != null && !imageUrl.trim().isEmpty()) {

            // Загружаем фотографию породы напрямую из интернета.
            Picasso.get()
                    .load(imageUrl.trim())
                    .noPlaceholder()
                    .into(breedImage);

        } else {

            // Если ссылки нет, показываем локальное фото.
            breedImage.setImageResource(fallbackImage);
        }

        // Кнопка возвращает в каталог пород.
        backButton.setOnClickListener(v -> finish());
    }

    // Выбираем запасную фотографию.
    private int getFallbackImage(int id) {

        switch (id) {

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
}
