plugins {
    id("com.bff.wespot.feature")
    id("com.bff.wespot.compose")
    id("com.bff.wespot.hilt")
}

android {
    namespace = "com.bff.wespot.auth"
}

dependencies {
    implementation(project(":domain"))
    implementation(libs.bundles.androidx.compose)
    implementation(libs.bundles.orbit)
    implementation(libs.junit)
    implementation(libs.androidx.junit)
}
