
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "ru.mirea.semina.catworld"

    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "ru.mirea.semina.catworld"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    // Модули Clean Architecture
    implementation(project(":domain"))
    implementation(project(":data"))

    // Основные библиотеки Android
    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)

    // Авторизация Firebase
    implementation(libs.firebase.auth)

    // Компоненты Material Design
    implementation(libs.material)

    // Выдвижное боковое меню Navigation Drawer
    implementation("androidx.drawerlayout:drawerlayout:1.2.0")

    // Тестирование
    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
}
