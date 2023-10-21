plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.kiryantsev.multiwidget"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.kiryantsev.multiwidget"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    //android core + lifecycle
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")

    //compose
    implementation("androidx.activity:activity-compose:1.7.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    //compose nav
    implementation("androidx.navigation:navigation-compose:2.7.4")

    //permission
    implementation ("com.google.accompanist:accompanist-permissions:0.33.1-alpha")

    //tests & compose tests
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // For AppWidgets support
    implementation ("androidx.glance:glance-appwidget:1.0.0")
    implementation ("androidx.glance:glance-material:1.0.0")
    implementation ("androidx.glance:glance-material3:1.0.0")

    //workmanager & co
    val work_version = "2.7.1"
    implementation("androidx.work:work-runtime-ktx:$work_version")
    implementation("androidx.work:work-gcm:$work_version")
    implementation ("androidx.work:work-multiprocess:$work_version")

    // Coroutines
    val coroutines_version = "1.5.2"
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_version")

    // Coroutine Lifecycle Scopes
    val coroutines_lifecycle_version = "2.3.1"
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:$coroutines_lifecycle_version")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:$coroutines_lifecycle_version")

    //Gson
    implementation ("com.google.code.gson:gson:2.8.7")

    // Room
    val room_version = "2.3.0"
    implementation ("androidx.room:room-runtime:$room_version")
    kapt ("androidx.room:room-compiler:$room_version")
    implementation ("androidx.room:room-ktx:$room_version")
    implementation("androidx.compose.runtime:runtime-livedata:1.4.3")

    //serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    // Retrofit
    implementation( "com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:okhttp:5.0.0-alpha.2")
    implementation ("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2")

    //hilt
    implementation ("com.google.dagger:hilt-android:2.44")
    kapt ("com.google.dagger:hilt-compiler:2.44")
    implementation ("androidx.hilt:hilt-work:1.0.0")

    //easy shared prefs
    implementation ("com.pixplicity.easyprefs:EasyPrefs:1.10.0")

    //user location
    implementation("com.google.android.gms:play-services-location:21.0.1")

    //kotlin datetime
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")

}

kapt {
    correctErrorTypes = true
}