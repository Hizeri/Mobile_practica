plugins {
    id("com.android.library")
}

android {
    namespace = "ru.mirea.semina.catworld.data"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        minSdk = 24

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

    // Data-слой использует интерфейсы и модели domain-слоя.
    implementation(project(":domain"))


    // Room — локальная база данных.
    implementation("androidx.room:room-runtime:2.8.3")

    // Firebase Authentication для data-слоя.
    implementation(libs.firebase.auth)
// Генерация кода Room для Java.
    annotationProcessor("androidx.room:room-compiler:2.8.3")

    implementation(libs.appcompat)
    implementation(libs.material)

    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
}