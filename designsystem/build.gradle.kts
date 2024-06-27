plugins {
    alias(libs.plugins.wespot.android.library)
    alias(libs.plugins.wespot.android.compose)
}

android {
    namespace = "com.bff.wespot.designsystem"
}

dependencies {
    implementation(project(":core:common"))
    implementation(libs.material)
}
