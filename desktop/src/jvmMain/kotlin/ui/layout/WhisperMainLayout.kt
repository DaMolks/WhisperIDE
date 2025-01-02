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
                            IconButton(onClick = { /* TODO: GitHub Token */ }) {
                                Icon(Icons.Default.Key, contentDescription = "GitHub Token")
                            }
                            IconButton(onClick = { /* TODO: Nouveau Projet */ }) {
                                Icon(Icons.Default.CreateNewFolder, contentDescription = "Nouveau Projet")
                            }
                            IconButton(onClick = { showSettings = true }) {
                                Icon(Icons.Default.Settings, contentDescription = "Paramètres")
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )

                    // Layout principal
                    Row(modifier = Modifier.fillMaxSize().weight(1f).padding(8.dp)) {
                        // Navigation des fichiers
                        ElevatedCard(
                            modifier = Modifier.width(250.dp).fillMaxHeight(),
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            elevation = CardDefaults.elevatedCardElevation(
                                defaultElevation = 3.dp
                            )
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(
                                    "Explorateur", 
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                // TODO: Ajouter l'arborescence des fichiers
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Zone principale
                        Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
                            // Zone de code
                            Card(
                                modifier = Modifier.weight(0.65f).fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                CodeEditorScreen()
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Zone de console
                            ElevatedCard(
                                modifier = Modifier.weight(0.35f).fillMaxWidth(),
                                colors = CardDefaults.elevatedCardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                ),
                                elevation = CardDefaults.elevatedCardElevation(
                                    defaultElevation = 3.dp
                                )
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text(
                                        "Console", 
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    TabRow(
                                        selectedTabIndex = 0,
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant
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

                        Spacer(modifier = Modifier.width(8.dp))

                        if (isChatVisible) {
                            ElevatedCard(
                                modifier = Modifier.width(400.dp).fillMaxHeight(),
                                colors = CardDefaults.elevatedCardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                ),
                                elevation = CardDefaults.elevatedCardElevation(
                                    defaultElevation = 3.dp
                                )
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            "Assistant IA", 
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        IconButton(onClick = { isChatVisible = false }) {
                                            Icon(
                                                Icons.Default.Close, 
                                                contentDescription = "Fermer",
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
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