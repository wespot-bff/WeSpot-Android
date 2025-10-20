plugins {
    alias(libs.plugins.wespot.android.feature)
    alias(libs.plugins.wespot.android.compose)
    alias(libs.plugins.wespot.android.hilt)
}

android {
    namespace = "com.bff.wespot.community.presentation"
}

ksp {
    arg("compose-destinations.moduleName", "community-presentation")
    arg("compose-destinations.mode", "destinations")
}

dependencies {
    implementation(project(":core:analytics"))
    implementation(project(":feature:community:ui-model"))
    implementation(project(":feature:server-driven"))

    implementation(libs.paging3)
    implementation(libs.bundles.orbit)
    implementation(libs.junit)
    implementation(libs.androidx.junit)
    implementation(libs.timber)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.lottie)
}
