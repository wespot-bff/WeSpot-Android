plugins {
    alias(libs.plugins.wespot.android.library)
}

android {
    namespace = "com.bff.wespot.paging"
}

dependencies {
    implementation(project(":core:model"))

    implementation(libs.junit)
    implementation(libs.timber)
    implementation(libs.paging3)
}
