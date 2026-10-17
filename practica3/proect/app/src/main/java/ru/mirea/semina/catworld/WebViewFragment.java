package ru.mirea.semina.catworld;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class WebViewFragment extends Fragment {

    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        // Подключаем XML-разметку
        View view = inflater.inflate(
                R.layout.fragment_web_view,
                container,
                false
        );

        // Находим встроенный браузер
        webView = view.findViewById(R.id.webView);

        // Открываем страницы внутри приложения
        webView.setWebViewClient(new WebViewClient());

        // Разрешаем JavaScript для современных сайтов
        webView.getSettings().setJavaScriptEnabled(true);

        // Загружаем сайт о породах кошек
        webView.loadUrl("https://cattish.ru/breed/");

        return view;
    }

    @Override
    public void onDestroyView() {
        // Освобождаем ресурсы браузера
        if (webView != null) {
            webView.destroy();
            webView = null;
        }

        super.onDestroyView();
    }
}
