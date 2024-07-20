plugins {
    alias(libs.plugins.wespot.android.library)
    alias(libs.plugins.wespot.android.hilt)
}

dependencies {
    implementation(project(":core:model"))

    implementation(libs.junit)
    implementation(libs.timber)
    implementation(libs.datastore)
}
