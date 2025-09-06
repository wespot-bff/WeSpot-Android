plugins {
    alias(libs.plugins.wespot.jvm.library)
}

dependencies {
    implementation(project(":core:model"))
}
