package ui.layout

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import ui.components.*
import ui.theme.WhisperTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhisperMainLayout() {
    var isChatVisible by remember { mutableStateOf(true) }

    WhisperTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            // Barre d'outils principale
            TopAppBar(
                title = { Text("WhisperIDE") },
                actions = {
                    IconButton(onClick = { /* TODO: Nouveau Projet */ }) {
                        Icon(Icons.Default.CreateNewFolder, contentDescription = "Nouveau Projet")
                    }
                    IconButton(onClick = { /* TODO: Paramètres */ }) {
                        Icon(Icons.Default.Settings, contentDescription = "Paramètres")
                    }
                }
            )

            // Layout principal
            Row(modifier = Modifier.fillMaxSize()) {
                // Navigation des fichiers (style explorateur)
                Card(
                    modifier = Modifier.width(250.dp).fillMaxHeight().padding(8.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text("Explorateur", style = MaterialTheme.typography.titleMedium)
                        // TODO: Ajouter l'arborescence des fichiers
                    }
                }

                // Zone principale de code/éditeur
                Card(
                    modifier = Modifier.weight(1f).fillMaxHeight().padding(8.dp)
                ) {
                    CodeEditorScreen()
                }

                // Panel de chat IA (redimensionnable)
                if (isChatVisible) {
                    Card(
                        modifier = Modifier.width(300.dp).fillMaxHeight().padding(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Assistant IA", style = MaterialTheme.typography.titleMedium)
                                IconButton(onClick = { isChatVisible = false }) {
                                    Icon(Icons.Default.Close, contentDescription = "Fermer")
                                }
                            }
                            ChatInterface()
                        }
                    }
                }
            }
        }
    }
}

enum class WhisperScreen(val title: String, val icon: ImageVector) {
    Chat("Chat", Icons.Default.Chat),
    Projects("Projets", Icons.Default.FolderOpen),
    Code("Code", Icons.Default.Code),
    Settings("Paramètres", Icons.Default.Settings)
}