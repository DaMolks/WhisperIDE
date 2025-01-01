plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "com.whisperide.desktop"

kotlin {
    jvm()
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(project(":shared"))
                implementation(compose.desktop.currentOs)
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.7.3")
                implementation(compose.runtime)
                implementation(compose.material3)
                implementation(compose.material)
                implementation(compose.materialIconsExtended) // Ajout pour les icônes
                implementation(compose.ui)
                implementation(compose.foundation)
                implementation(compose.animation)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
    }
}