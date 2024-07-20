plugins {
    alias(libs.plugins.wespot.android.library)
    alias(libs.plugins.wespot.android.hilt)
}

android {
    namespace = "com.bff.wespot.data.local"
}

dependencies {
    implementation(project(":core:model"))

    implementation(libs.junit)
    implementation(libs.timber)
    implementation(libs.datastore)
}
