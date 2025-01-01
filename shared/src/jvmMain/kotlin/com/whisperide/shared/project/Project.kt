package com.whisperide.shared.project

import java.io.File
import java.time.LocalDateTime

data class Project(
    val name: String,
    val path: File,
    val githubRepo: String? = null,
    val lastOpened: LocalDateTime = LocalDateTime.now(),
    val config: ProjectConfig = ProjectConfig()
) {
    val configFile: File
        get() = File(path, ".whisperide/config.json")
        
    val isGitLinked: Boolean
        get() = File(path, ".git").exists()
}

data class ProjectConfig(
    val autoSync: Boolean = true,
    val branch: String = "main",
    val buildCommand: String = "gradle build",
    val excludedPaths: List<String> = listOf(".whisperide", "build", ".gradle")
)