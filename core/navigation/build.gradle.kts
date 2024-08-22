plugins {
    alias(libs.plugins.wespot.android.library)
    alias(libs.plugins.wespot.android.hilt)
}


android {
    namespace = "com.bff.wespot.navigation"
}

dependencies {
    implementation(libs.androidx.compose.destination)
}
