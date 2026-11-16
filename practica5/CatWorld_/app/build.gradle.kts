
import java.util.Properties

// Подключаем плагины Android и Google Services.
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

// Читаем файл local.properties из корня проекта.
val localProperties = Properties().apply {

    // Находим файл с локальными настройками.
    val propertiesFile = rootProject.file("local.properties")

    // Если файл существует, читаем его содержимое.
    if (propertiesFile.exists()) {
        propertiesFile.inputStream().use { load(it) }
    }
}

// Получаем API-ключ для The Cat API.
// Если ключ не найден, используем пустую строку.
val catApiKey = localProperties.getProperty("CAT_API_KEY", "")

android {

    namespace = "ru.mirea.semina.catworld"
    compileSdk { version = release(37) }

    defaultConfig {
        applicationId = "ru.mirea.semina.catworld"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Передаём API-ключ в строковые ресурсы Android.
        resValue("string", "cat_api_key", catApiKey)
    }

    // Разрешаем Gradle создавать строковые ресурсы через resValue().
    buildFeatures {
        resValues = true
    }

    // Настройки сборки приложения.
    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }

    // Используем Java 11.
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    // Подключаем наши модули Clean Architecture.
    implementation(project(":domain"))
    implementation(project(":data"))

    // Picasso — загрузка фотографий из интернета.
    implementation("com.squareup.picasso:picasso:2.8")

    // Стандартные библиотеки Android.
    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)

    // Firebase — авторизация пользователей.
    implementation(libs.firebase.auth)

    // Material Design — оформление интерфейса.
    implementation(libs.material)

    // ViewModel — логика экрана.
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.8.7")

    // LiveData — наблюдение за изменениями данных.
    implementation("androidx.lifecycle:lifecycle-livedata:2.8.7")

    // RecyclerView — каталог пород кошек.
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // Библиотеки для тестирования.
    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
}
