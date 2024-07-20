plugins {
    alias(libs.plugins.wespot.android.feature)
    alias(libs.plugins.wespot.android.compose)
    alias(libs.plugins.wespot.android.hilt)
}

android {
    namespace = "com.bff.wespot.vote"
}

ksp {
    arg("compose-destinations.moduleName", "vote")
    arg("compose-destinations.mode", "destinations")
}

dependencies {
    implementation(libs.bundles.orbit)
    implementation(libs.junit)
    implementation(libs.androidx.junit)
    implementation(libs.timber)
}
