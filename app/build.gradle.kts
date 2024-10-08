plugins {
    id("com.android.application")
}

android {
    namespace = "IdoMizrahi.fitnessassistant"
    compileSdk = 34

    defaultConfig {
        applicationId = "IdoMizrahi.fitnessassistant"
        minSdk = 26
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(project(mapOf("path" to ":ViewModel")))
    implementation(project(mapOf("path" to ":Model")))
    implementation(project(mapOf("path" to ":Helper")))
    implementation("androidx.preference:preference:1.2.1")
    implementation("androidx.activity:activity:1.8.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.google.code.gson:gson:2.10.1")
    implementation ("com.squareup.retrofit:retrofit:2.0.0-beta2")
    implementation("com.squareup.retrofit:converter-gson:2.0.0-beta2")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation("com.google.firebase:firebase-firestore:25.0.0")

    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")


    val nav_version = "2.7.7"
    implementation("androidx.navigation:navigation-fragment:$nav_version")
    implementation("androidx.navigation:navigation-ui:$nav_version")

    implementation("org.jsoup:jsoup:1.14.3")


    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    implementation("com.squareup.picasso:picasso:2.71828")
    implementation("jp.wasabeef:picasso-transformations:2.2.1")

}
