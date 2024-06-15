plugins {
    id("com.bff.wespot.android.library")
    id("com.bff.wespot.compose")
}

android {
    namespace = "com.bff.wespot.ui"
}

dependencies {
    implementation(libs.material)
}