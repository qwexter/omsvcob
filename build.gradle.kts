import kotlinx.benchmark.gradle.JvmBenchmarkTarget

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.benchmark)
}

group = "xyz.qwexter"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.benchmark)
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
}

tasks.test {
    useJUnitPlatform()
}

benchmark {
    targets {
        register("main") {
            this as JvmBenchmarkTarget
            jmhVersion = "1.37"
        }
    }
}