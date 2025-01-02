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
    var isDarkTheme by remember { mutableStateOf(false) }
    var showSettings by remember { mutableStateOf(false) }

    // Initialisation du thème système au premier rendu
    LaunchedEffect(Unit) {
        isDarkTheme = isSystemInDarkTheme()
    }

    WhisperTheme(darkTheme = isDarkTheme) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (showSettings) {
                SettingsScreen(
                    isDarkTheme = isDarkTheme,
                    onThemeChanged = { isDarkTheme = it }
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
                        }
                    )

                    // Layout principal
                    Row(modifier = Modifier.fillMaxSize().weight(1f)) {
                        // Navigation des fichiers (style explorateur)
                        Card(
                            modifier = Modifier.width(250.dp).fillMaxHeight().padding(8.dp)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text("Explorateur", style = MaterialTheme.typography.titleMedium)
                                // TODO: Ajouter l'arborescence des fichiers
                            }
                        }

                        // Zone principale de code et output
                        Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
                            // Éditeur de code (2/3 de l'espace)
                            Card(
                                modifier = Modifier.weight(0.65f).fillMaxWidth().padding(8.dp)
                            ) {
                                CodeEditorScreen()
                            }

                            // Zone de sortie/logs/preview (1/3 de l'espace)
                            Card(
                                modifier = Modifier.weight(0.35f).fillMaxWidth().padding(8.dp)
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text("Console / Preview", style = MaterialTheme.typography.titleMedium)
                                    // Tabs pour différentes vues
                                    TabRow(selectedTabIndex = 0) {
                                        Tab(selected = true, onClick = { }, text = { Text("Console") })
                                        Tab(selected = false, onClick = { }, text = { Text("Sortie") })
                                        Tab(selected = false, onClick = { }, text = { Text("Preview") })
                                    }
                                    // Zone de contenu
                                    Box(modifier = Modifier.fillMaxSize().padding(top = 8.dp)) {
                                        Text("// Sortie du programme s'affichera ici", style = MaterialTheme.typography.bodyMedium)
                                    }
                                }
                            }
                        }

                        // Panel de chat IA (plus large)
                        if (isChatVisible) {
                            Card(
                                modifier = Modifier.width(400.dp).fillMaxHeight().padding(8.dp)
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