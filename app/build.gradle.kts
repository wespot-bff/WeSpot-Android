plugins {
    id("com.bff.wespot.application")
    id("com.bff.wespot.hilt")
    id("com.bff.wespot.compose")
    id("com.bff.wespot.firebase")
}

android {
    namespace = "com.bff.wespot"
}

dependencies {
    implementation(project(":feature:auth"))
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":core:model"))
    implementation(project(":feature:auth"))
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.junit)
}