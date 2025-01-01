import com.whisperide.shared.project.ProjectManager
import github.GithubAuth
import github.GithubManager
import java.io.File

// Instances globales pour la gestion de GitHub
val githubAuth = GithubAuth()
val githubManager = GithubManager(githubAuth)

// Instance globale pour la gestion des projets
val projectManager = ProjectManager(
    workspacePath = File(System.getProperty("user.home"), "WhisperIDE/Projects")
)