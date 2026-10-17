
package ru.mirea.semina.catworld;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

public class CatWorldDrawerActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_world_drawer);

        // Находим элементы бокового меню
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        ImageButton buttonMenu = findViewById(R.id.buttonMenu);

        // Открываем боковое меню
        buttonMenu.setOnClickListener(v ->
                drawerLayout.openDrawer(GravityCompat.START)
        );

        // Обрабатываем выбранный пункт меню
        navigationView.setNavigationItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_data) {

                // Показываем информацию о кошках
                showFragment(new DataFragment());
                navigationView.setCheckedItem(id);

            } else if (id == R.id.nav_web) {

                // Открываем встроенный браузер
                showFragment(new WebViewFragment());
                navigationView.setCheckedItem(id);

            } else if (id == R.id.nav_back) {

                // Закрываем экран и возвращаемся в CatWorld
                finish();
                return true;
            }

            // Закрываем меню после выбора
            drawerLayout.closeDrawer(GravityCompat.START);

            return true;
        });

        // При первом открытии показываем DataFragment
        if (savedInstanceState == null) {
            showFragment(new DataFragment());
            navigationView.setCheckedItem(R.id.nav_data);
        }

        // Обрабатываем системную кнопку Назад
        getOnBackPressedDispatcher().addCallback(
                this,
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {

                        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {

                            // Если меню открыто, сначала закрываем его
                            drawerLayout.closeDrawer(GravityCompat.START);

                        } else {

                            // Иначе возвращаемся на главный экран
                            finish();
                        }
                    }
                }
        );
    }

    // Отображаем выбранный фрагмент
    private void showFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}
