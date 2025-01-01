package ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.whisperide.shared.project.Project
import com.whisperide.shared.project.ProjectManager
import kotlinx.coroutines.launch

@Composable
fun ProjectDialog(
    projectManager: ProjectManager,
    onDismiss: () -> Unit
) {
    var projects by remember { mutableStateOf<List<Project>>(emptyList()) }
    var newProjectName by remember { mutableStateOf("") }
    var newProjectGithub by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    
    val scope = rememberCoroutineScope()
    
    // Charger la liste des projets au démarrage
    LaunchedEffect(Unit) {
        projects = projectManager.listProjects()
    }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Gestionnaire de Projets") },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 8.dp)
            ) {
                // Liste des projets existants
                Text(
                    "Projets existants :",
                    style = MaterialTheme.typography.h6,
                    color = Color.White
                )
                Spacer(Modifier.height(8.dp))
                
                if (projects.isEmpty()) {
                    Text(
                        "Aucun projet existant",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                } else {
                    projects.forEach { project ->
                        ProjectItem(
                            project = project,
                            onClick = {
                                scope.launch {
                                    projectManager.openProject(project)
                                    onDismiss()
                                }
                            }
                        )
                    }
                }
                
                // Création d'un nouveau projet
                Spacer(Modifier.height(24.dp))
                Text(
                    "Nouveau projet",
                    style = MaterialTheme.typography.h6,
                    color = Color.White
                )
                Spacer(Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = newProjectName,
                    onValueChange = { 
                        newProjectName = it
                        error = null
                    },
                    label = { Text("Nom du projet") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = newProjectGithub,
                    onValueChange = { newProjectGithub = it },
                    label = { Text("Repository GitHub (optionnel)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                error?.let {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        it,
                        color = MaterialTheme.colors.error,
                        fontSize = 12.sp
                    )
                }
                
                if (isLoading) {
                    Spacer(Modifier.height(8.dp))
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    scope.launch {
                        try {
                            if (newProjectName.isBlank()) {
                                error = "Le nom du projet est requis"
                                return@launch
                            }
                            
                            isLoading = true
                            error = null
                            
                            val githubRepo = newProjectGithub.takeIf { it.isNotBlank() }
                            val project = projectManager.createProject(
                                name = newProjectName,
                                githubRepo = githubRepo
                            )
                            
                            projectManager.openProject(project)
                            onDismiss()
                        } catch (e: Exception) {
                            error = e.message
                        } finally {
                            isLoading = false
                        }
                    }
                },
                enabled = !isLoading
            ) {
                Text("Créer le projet")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isLoading
            ) {
                Text("Annuler")
            }
        }
    )
}

@Composable
private fun ProjectItem(
    project: Project,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        backgroundColor = Color(0xFF34495E)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    project.name,
                    color = Color.White,
                    fontSize = 14.sp
                )
                project.githubRepo?.let { repo ->
                    Text(
                        repo,
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
            }
            if (project.isGitLinked) {
                Text("🔗", fontSize = 16.sp)
            }
        }
    }
}
