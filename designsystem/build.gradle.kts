plugins {
    alias(libs.plugins.wespot.android.library)
    alias(libs.plugins.wespot.android.compose)
    alias(libs.plugins.wespot.android.ktlint)
}

android {
    namespace = "com.bff.wespot.designsystem"
}

dependencies {
    implementation(project(":core:common"))
    implementation(libs.material)
    implementation(libs.kotlinx.collections.immutable)
}
