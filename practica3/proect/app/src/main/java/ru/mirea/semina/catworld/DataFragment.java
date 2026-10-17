
package ru.mirea.semina.catworld;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DataFragment extends Fragment {

    // Создаём интерфейс фрагмента
    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        // Подключаем разметку с информацией о кошках
        return inflater.inflate(
                R.layout.fragment_data,
                container,
                false
        );
    }
}
