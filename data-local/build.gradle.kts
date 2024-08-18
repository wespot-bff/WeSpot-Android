import com.google.protobuf.gradle.GenerateProtoTask
import org.gradle.configurationcache.extensions.capitalized
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.wespot.android.library)
    alias(libs.plugins.wespot.android.hilt)
    alias(libs.plugins.google.protobuf)
}

android {
    namespace = "com.bff.wespot.data.local"
}

protobuf {
    protoc {
        artifact = libs.protobuf.protoc.get().toString()
    }

    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                register("kotlin") {
                    option("lite")
                }
                register("java") {
                    option("lite")
                }
            }
        }
    }
}

androidComponents {
    onVariants(selector().all()) { variant ->
        afterEvaluate {
            val capName = variant.name.capitalized()
            tasks.getByName<KotlinCompile>("ksp${capName}Kotlin") {
                setSource(tasks.getByName("generate${capName}Proto").outputs)
            }
        }
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))

    implementation(libs.junit)
    implementation(libs.timber)
    implementation(libs.datastore)
    implementation(libs.datastore.prefereces)
    implementation(libs.kotlin.coroutines)
    implementation(libs.protobuf.kotlin.lite)
}
