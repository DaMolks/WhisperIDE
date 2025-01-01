package com.whisperide.shared.project

import kotlinx.serialization.Serializable
import java.io.File
import java.time.LocalDateTime

@Serializable
data class Project(
    val name: String,
    val path: String,  // On utilise String au lieu de File pour la sérialisation
    val githubRepo: String? = null,
    val lastOpened: String = LocalDateTime.now().toString(),  // DateTime en String pour la sérialisation
    val config: ProjectConfig = ProjectConfig()
) {
    val pathFile: File
        get() = File(path)
        
    val configFile: File
        get() = File(pathFile, ".whisperide/config.json")
        
    val isGitLinked: Boolean
        get() = File(pathFile, ".git").exists()
}

@Serializable
data class ProjectConfig(
    val autoSync: Boolean = true,
    val branch: String = "main",
    val buildCommand: String = "gradle build",
    val excludedPaths: List<String> = listOf(".whisperide", "build", ".gradle")
)