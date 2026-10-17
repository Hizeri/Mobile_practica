
package ru.mirea.semina.simplefragmentapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FirstFragment extends Fragment {

    // Создаём интерфейс первого фрагмента
    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        // Загружаем XML-разметку первого экрана
        return inflater.inflate(
                R.layout.fragment_first,
                container,
                false
        );
    }
}
