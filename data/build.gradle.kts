plugins {
    id("com.bff.wespot.library")
    id("com.bff.wespot.hilt")
}

android {
    namespace = "com.bff.wespot.data"
}

dependencies {
    implementation(libs.junit)
    implementation(libs.androidx.junit)
}
