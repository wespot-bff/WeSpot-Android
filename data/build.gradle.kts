plugins {
    alias(libs.plugins.wespot.android.library)
    alias(libs.plugins.wespot.android.hilt)
}

android {
    namespace = "com.bff.wespot.data"
}

dependencies {
    implementation(libs.junit)
    implementation(project(":domain"))
    implementation(project(":core:network"))
    implementation(project(":core:model"))
}
