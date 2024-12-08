plugins {
    alias(libs.plugins.wespot.jvm.library)
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation(project(":core:model"))
    testImplementation(libs.junit.jupitor)
}
