package com.whisperide.shared.project

import java.io.File

// Instance globale du ProjectManager
val projectManager = ProjectManager(
    File(System.getProperty("user.home"), "WhisperIDE/Projects")
)