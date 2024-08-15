plugins {
    alias(libs.plugins.wespot.android.library)
    alias(libs.plugins.wespot.android.compose)
    alias(libs.plugins.wespot.android.hilt)
}

android {
    namespace = "com.bff.wespot.analytics"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.timber)
}