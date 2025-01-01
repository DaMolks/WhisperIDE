package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.io.File

// Modèle de projet
data class ProjectInfo(
    val name: String,
    val path: String,
    val lastModified: Long = System.currentTimeMillis(),
    val language: String = "Kotlin"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectManagementScreen() {
    var projects by remember { mutableStateOf(listOf<ProjectInfo>()) }
    var isCreatingProject by remember { mutableStateOf(false) }
    var newProjectName by remember { mutableStateOf("") }

    // Simuler la récupération des projets (à remplacer par une vraie logique)
    LaunchedEffect(Unit) {
        projects = listOf(
            ProjectInfo("Projet Example 1", "/path/to/project1"),
            ProjectInfo("Projet Example 2", "/path/to/project2")
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // En-tête de la section Projets
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Mes Projets", style = MaterialTheme.typography.headlineMedium)
            
            IconButton(onClick = { isCreatingProject = true }) {
                Icon(Icons.Default.Add, contentDescription = "Créer un nouveau projet")
            }
        }

        // Liste des projets
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 16.dp)
        ) {
            items(projects) { project ->
                ProjectItem(
                    project = project,
                    onSelect = { /* TODO: Logique de sélection de projet */ },
                    onDelete = { /* TODO: Logique de suppression de projet */ }
                )
            }
        }

        // Dialogue de création de projet
        if (isCreatingProject) {
            AlertDialog(
                onDismissRequest = { isCreatingProject = false },
                title = { Text("Créer un nouveau projet") },
                text = {
                    TextField(
                        value = newProjectName,
                        onValueChange = { newProjectName = it },
                        label = { Text("Nom du projet") }
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        // TODO: Logique de création de projet
                        projects += ProjectInfo(newProjectName, "/path/to/new/project")
                        isCreatingProject = false
                        newProjectName = ""
                    }) {
                        Text("Créer")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { isCreatingProject = false }) {
                        Text("Annuler")
                    }
                }
            )
        }
    }
}

@Composable
fun ProjectItem(
    project: ProjectInfo,
    onSelect: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onSelect),
        colors = CardDefaults.cardColors(
            containerColor = Color.DarkGray.copy(alpha = 0.2f)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(project.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    "${project.language} • ${File(project.path).absolutePath}", 
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Row {
                IconButton(onClick = onSelect) {
                    Icon(Icons.Default.OpenInNew, contentDescription = "Ouvrir")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Supprimer")
                }
            }
        }
    }
}