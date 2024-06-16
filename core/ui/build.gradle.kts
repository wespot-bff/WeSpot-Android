plugins {
    alias(libs.plugins.wespot.android.feature)
    alias(libs.plugins.wespot.android.compose)
}

android {
    namespace = "com.bff.wespot.ui"
}

dependencies {
    implementation(libs.material)
    implementation(project(":designsystem"))
    implementation(project(":core:model"))
}
