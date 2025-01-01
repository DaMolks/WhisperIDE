package com.whisperide.shared.project

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.whisperide.shared.utils.SecureStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.File

class ProjectManager(private val workspacePath: File) {
    private var _currentProject by mutableStateOf<Project?>(null)
    private val storage = SecureStorage(File(System.getProperty("user.home"), ".whisperide"))
    
    val currentProject: Project?
        get() = _currentProject
        
    init {
        workspacePath.mkdirs()
    }
    
    suspend fun listProjects(): List<Project> = withContext(Dispatchers.IO) {
        workspacePath.listFiles()
            ?.filter { it.isDirectory && File(it, ".whisperide").exists() }
            ?.map { loadProject(it) }
            ?: emptyList()
    }
    
    private fun loadProject(projectDir: File): Project {
        val configFile = File(projectDir, ".whisperide/config.json")
        val config = if (configFile.exists()) {
            Json.decodeFromString<ProjectConfig>(configFile.readText())
        } else {
            ProjectConfig()
        }
        
        return Project(
            name = projectDir.name,
            path = projectDir,
            config = config
        )
    }
    
    suspend fun createProject(
        name: String,
        githubRepo: String? = null
    ): Project = withContext(Dispatchers.IO) {
        val projectDir = File(workspacePath, name)
        if (projectDir.exists()) {
            throw IllegalArgumentException("Un projet avec ce nom existe déjà")
        }
        
        // Créer la structure du projet
        projectDir.mkdirs()
        File(projectDir, ".whisperide").mkdirs()
        File(projectDir, "src").mkdirs()
        
        // Créer le projet
        val project = Project(name, projectDir, githubRepo)
        
        // Sauvegarder la configuration
        saveProjectConfig(project)
        
        project
    }
    
    private fun saveProjectConfig(project: Project) {
        val configJson = Json.encodeToString(ProjectConfig.serializer(), project.config)
        project.configFile.writeText(configJson)
    }
    
    suspend fun openProject(project: Project) = withContext(Dispatchers.IO) {
        _currentProject = project
        // TODO: Synchroniser avec GitHub si nécessaire
    }
    
    fun closeCurrentProject() {
        _currentProject = null
    }
}
