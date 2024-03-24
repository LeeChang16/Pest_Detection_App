plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.pestdetectionapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.pestdetectionapp"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        mlModelBinding = true
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("org.tensorflow:tensorflow-lite-support:0.1.0")
    implementation("org.tensorflow:tensorflow-lite-metadata:0.1.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation ("androidx.camera:camera-core:1.2.2")
    implementation ("androidx.camera:camera-camera2:1.2.2")
    implementation ("androidx.camera:camera-lifecycle:1.2.2")
    implementation ("androidx.camera:camera-view:1.2.2")


    implementation ("com.etebarian:meow-bottom-navigation:1.2.0")


    implementation ("org.tensorflow:tensorflow-lite:2.14.0")
    // The GPU delegate library is optional. Depend on it as needed
    implementation ("org.tensorflow:tensorflow-lite-gpu:2.14.0");
    implementation ("org.tensorflow:tensorflow-lite-support:0.4.4");
    implementation ("org.tensorflow:tensorflow-lite-metadata:0.4.4");
    implementation ("org.tensorflow:tensorflow-lite:2.4.0")


//    implementation ("com.android.support:design:29.0.0-alpha3");
//    implementation ("com.android.support:design:29.0.0-alpha1");

    implementation ("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation ("com.google.code.gson:gson:2.8.6")
    androidTestImplementation ("androidx.test.ext:junit:1.1.1")
    androidTestImplementation ("com.android.support.test:rules:1.0.2")
    androidTestImplementation ("com.google.truth:truth:1.0.1")

}