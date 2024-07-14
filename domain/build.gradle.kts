plugins {
    alias(libs.plugins.wespot.jvm.library)
}

dependencies {
    implementation(libs.junit)
}

dependencies {
    implementation(libs.java.inject)
    implementation(project(":core:model"))
}
