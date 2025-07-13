plugins {
    alias(libs.plugins.wespot.android.library)
    alias(libs.plugins.wespot.android.hilt)
}


android {
    namespace = "com.bff.wespot.navigation"
}

dependencies {
    implementation(project(":core:model"))
    implementation(libs.androidx.compose.destination)
}
