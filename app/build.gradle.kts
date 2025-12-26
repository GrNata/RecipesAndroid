import org.gradle.kotlin.dsl.implementation

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.grig.recipesandroid"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.grig.recipesandroid"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    // Core
    implementation("androidx.core:core-ktx:1.13.1")

    // Jetpack Compose
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("androidx.compose.ui:ui:1.7.4")
    implementation("androidx.compose.material3:material3:1.3.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.7.4")
    debugImplementation("androidx.compose.ui:ui-tooling:1.7.4")



    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
    //    Для фильтрации через StateFlow и debounce можно добавить (опционально)
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // Retrofit + Gson
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // OkHttp
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.8.3")

    // Images
    implementation("io.coil-kt:coil-compose:2.7.0")

//    Paging
    implementation("androidx.paging:paging-runtime:3.2.0")                  // Paging для ViewModel
    implementation("androidx.paging:paging-compose:1.0.0-alpha18")          // Paging для Compose


//    Pull-to-refresh (возможность обновлять список рецептов жестом вниз) - вариант с Material3 PullRefresh
//    implementation("androidx.xr.compose.material3:material3:1.0.0-alpha13")
//    Pull-to-refresh (возможность обновлять список рецептов жестом вниз) - вариант с Google
//    implementation("com.google.accompanist:accompanist-swiperefresh:0.31.1-alpha")
    implementation("com.google.accompanist:accompanist-swiperefresh:0.36.0")

////    Accompanist Navigation Animation, Accompanist Shared Element
//    implementation("com.google.accompanist:accompanist-navigation-animation:0.36.0")
//    implementation("com.google.accompanist:accompanist-navigation-material:0.36.0")
////    implementation("com.google.accompanist:accompanist-shared-element:0.31.5-beta")
//    implementation("com.google.accompanist:accompanist-shared-element:0.37.3")

}