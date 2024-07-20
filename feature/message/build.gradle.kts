plugins {
    alias(libs.plugins.wespot.android.feature)
    alias(libs.plugins.wespot.android.compose)
    alias(libs.plugins.wespot.android.hilt)
}

android {
    namespace = "com.bff.wespot.message"
}

dependencies {
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.bundles.androidx.compose)
    implementation(libs.bundles.orbit)
    implementation(libs.junit)
    implementation(libs.androidx.junit)
}
