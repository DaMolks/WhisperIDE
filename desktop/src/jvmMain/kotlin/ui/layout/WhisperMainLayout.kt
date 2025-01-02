package ui.layout

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import ui.components.*
import ui.screens.*
import ui.theme.WhisperTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhisperMainLayout() {
    var isChatVisible by remember { mutableStateOf(true) }
    var showSettings by remember { mutableStateOf(false) }
    
    // Gestion du thème
    val systemInDarkTheme = isSystemInDarkTheme()
    var isDarkTheme by remember { mutableStateOf(systemInDarkTheme) }

    WhisperTheme(darkTheme = isDarkTheme) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            if (showSettings) {
                SettingsScreen(
                    isDarkTheme = isDarkTheme,
                    onThemeChanged = { isDarkTheme = it },
                    onBack = { showSettings = false }
                )
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Barre d'outils principale
                    TopAppBar(
                        title = { Text("WhisperIDE") },
                        actions = {
                            IconButton(onClick = { /* TODO: Nouveau Projet */ }) {
                                Icon(Icons.Default.CreateNewFolder, contentDescription = "Nouveau Projet")
                            }
                            IconButton(onClick = { showSettings = true }) {
                                Icon(Icons.Default.Settings, contentDescription = "Paramètres")
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            titleContentColor = MaterialTheme.colorScheme.onSurface
                        )
                    )

                    // Layout principal
                    Row(modifier = Modifier.fillMaxSize().weight(1f)) {
                        // Navigation des fichiers
                        Card(
                            modifier = Modifier.width(250.dp).fillMaxHeight().padding(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text("Explorateur", style = MaterialTheme.typography.titleMedium)
                                // TODO: Ajouter l'arborescence des fichiers
                            }
                        }

                        // Zone principale
                        Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
                            Card(
                                modifier = Modifier.weight(0.65f).fillMaxWidth().padding(8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                )
                            ) {
                                CodeEditorScreen()
                            }

                            Card(
                                modifier = Modifier.weight(0.35f).fillMaxWidth().padding(8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                )
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text("Console", style = MaterialTheme.typography.titleMedium)
                                    TabRow(
                                        selectedTabIndex = 0,
                                        containerColor = MaterialTheme.colorScheme.surface
                                    ) {
                                        Tab(selected = true, onClick = { }, text = { Text("Console") })
                                        Tab(selected = false, onClick = { }, text = { Text("Sortie") })
                                        Tab(selected = false, onClick = { }, text = { Text("Preview") })
                                    }
                                    Box(modifier = Modifier.fillMaxSize().padding(top = 8.dp)) {
                                        Text(
                                            "// Sortie du programme s'affichera ici",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }

                        if (isChatVisible) {
                            Card(
                                modifier = Modifier.width(400.dp).fillMaxHeight().padding(8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                )
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
    }
}

enum class WhisperScreen(val title: String, val icon: ImageVector) {
    Chat("Chat", Icons.Default.Chat),
    Projects("Projets", Icons.Default.FolderOpen),
    Code("Code", Icons.Default.Code),
    Settings("Paramètres", Icons.Default.Settings)
}