plugins {
    alias(libs.plugins.wespot.android.feature)
    alias(libs.plugins.wespot.android.compose)
}

android {
    namespace = "com.bff.wespot.designsystem"
}

dependencies {
    implementation(libs.material)
}
