import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

android {
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    namespace = "com.example.tablia"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.tablia"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "API_KEY", localProperties.getProperty("API_KEY") ?: "\"1\"")
        buildConfigField("String", "BASE_URL", localProperties.getProperty("BASE_URL") ?: "\"https://www.themealdb.com/api/json/v1/\"")
        buildConfigField("String", "WEB_CLIENT_ID", localProperties.getProperty("WEB_CLIENT_ID") ?: "\"\"")
        buildConfigField("String", "PREF_NAME", localProperties.getProperty("PREF_NAME") ?: "\"TabliaPrefs\"")
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.lottie)
    implementation(libs.fragment)
    implementation(libs.core.splashscreen)
    
    // Navigation
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:34.8.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-storage")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.tbuonomo:dotsindicator:5.1.0")
    implementation("androidx.credentials:credentials:1.3.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.3.0")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
    implementation("com.github.bumptech.glide:glide:5.0.5")

    //rxjava
    implementation("io.reactivex.rxjava3:rxjava:3.1.6")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.2")


    val room_version = "2.8.4"


    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")


    //retrofit with RXJava
    implementation("com.github.akarnokd:rxjava3-retrofit-adapter:3.0.0")

    //room with RXJava
    implementation("androidx.room:room-rxjava3:$room_version")

    implementation("com.f2prateek.rx.preferences2:rx-preferences:2.0.1")


    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
