plugins {
    alias(libs.plugins.wespot.android.feature)
    alias(libs.plugins.wespot.android.compose)
    alias(libs.plugins.wespot.android.hilt)
}

android {
    namespace = "com.bff.wespot.message"
}

ksp {
    arg("compose-destinations.moduleName", "message")
    arg("compose-destinations.mode", "destinations")
}

dependencies {
    implementation(project(":core:analytics"))
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.bundles.orbit)
    implementation(libs.junit)
    implementation(libs.androidx.junit)
    implementation(libs.lottie)
    implementation(libs.coil.core)
    implementation(libs.coil.compose)
    implementation(libs.paging3)
    implementation(libs.timber)
}
