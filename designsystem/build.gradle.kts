plugins {
    id("com.bff.wespot.feature")
    id("com.bff.wespot.compose")
}

android {
    namespace = "com.bff.wespot.designsystem"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
}
