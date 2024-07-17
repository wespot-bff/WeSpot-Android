plugins {
    alias(libs.plugins.wespot.jvm.library)
}

dependencies {
    implementation(libs.junit)
}

dependencies {
    implementation(project(":core:model"))

    implementation(libs.java.inject)
}
