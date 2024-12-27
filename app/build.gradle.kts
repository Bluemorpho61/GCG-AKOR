plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
}

android {
    namespace = "com.alkindi.gcg_akor"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.alkindi.gcg_akor"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
//    implementation(libs.navigation.fragment.ktx)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.fragment.ktx)


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation (libs.androidx.core.splashscreen)
    implementation (libs.kotlinx.coroutines.android)
    implementation (libs.circleimageview)
    implementation(libs.glide)
    implementation(libs.retrofit)
    implementation ("com.github.roiacult:BackdropLayout:v2.3")
    implementation(libs.material.v1130alpha05)
    implementation(libs.androidx.constraintlayout)

//    Library bwt networking API
    implementation("com.github.bumptech.glide:glide:4.11.0")
    implementation ("com.github.bumptech.glide:annotations:4.11.0")
    implementation ("com.github.bumptech.glide:okhttp3-integration:4.11.0")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

//    Library bwt implementasi DataStore (local)
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.6")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
//    implementation ("androidx.room:room-runtime:$room_version")
    implementation(libs.androidx.room.runtime)


//    annotationProcessor ("androidx.room:room-compiler:$room_version")
    annotationProcessor("com.github.bumptech.glide:compiler:4.11.0")

//    testImplementation ("androidx.room:room-testing:$room_version")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    ksp(libs.room.compiler)
}