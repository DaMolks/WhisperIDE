plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "com.whisperIDE.shared"

kotlin {
    jvm()
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
            }
        }
    }
}