plugins {
    alias(libs.plugins.wespot.android.library)
    alias(libs.plugins.wespot.android.compose)
}

android {
    namespace = "com.bff.wespot.ui"
}

dependencies {
    implementation(libs.material)
    implementation(project(":designsystem"))
    implementation(project(":core:model"))
    implementation(project(":core:common"))
}
