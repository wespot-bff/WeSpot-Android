plugins {
    alias(libs.plugins.wespot.android.feature)
    alias(libs.plugins.wespot.android.compose)
    alias(libs.plugins.wespot.android.hilt)
}

android {
    namespace = "com.bff.wespot.vote"
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":core:model"))
    implementation(project(":core:ui"))
    implementation(project(":designsystem"))

    implementation(libs.bundles.androidx.compose)
    implementation(libs.bundles.orbit)
    implementation(libs.junit)
    implementation(libs.androidx.junit)
    implementation(libs.timber)
}
