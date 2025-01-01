plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "com.whisperIDE.desktop"

kotlin {
    jvm()
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(project(":shared"))
                implementation(compose.desktop.currentOs)
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
    }
}