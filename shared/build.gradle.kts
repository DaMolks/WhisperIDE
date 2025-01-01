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
                implementation(compose.desktop.currentOs)
            }
        }
    }
}