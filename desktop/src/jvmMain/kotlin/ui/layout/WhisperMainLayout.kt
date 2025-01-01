package ui.layout

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.theme.WhisperTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhisperMainLayout() {
    var selectedScreen by remember { mutableStateOf(WhisperScreen.Chat) }
    var isDrawerOpen by remember { mutableStateOf(false) }

    WhisperTheme {
        Scaffold(
            topBar = {
                SmallTopAppBar(
                    title = { Text("WhisperIDE") },
                    navigationIcon = {
                        IconButton(onClick = { isDrawerOpen = !isDrawerOpen }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* Paramètres */ }) {
                            Icon(Icons.Default.Settings, contentDescription = "Paramètres")
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    WhisperScreen.values().forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            selected = selectedScreen == screen,
                            onClick = { selectedScreen = screen }
                        )
                    }
                }
            },
            drawerContent = {
                ModalDrawerSheet {
                    Text("WhisperIDE", modifier = Modifier.padding(16.dp))
                    Divider()
                    NavigationDrawerItem(
                        label = { Text("Fichiers") },
                        icon = { Icon(Icons.Default.Folder, contentDescription = "Fichiers") },
                        selected = false,
                        onClick = { /* Ouvrir explorateur de fichiers */ }
                    )
                    NavigationDrawerItem(
                        label = { Text("Thème") },
                        icon = { Icon(Icons.Default.DarkMode, contentDescription = "Thème") },
                        selected = false,
                        onClick = { /* Changer de thème */ }
                    )
                }
            },
            content = { padding ->
                Box(modifier = Modifier.padding(padding)) {
                    when (selectedScreen) {
                        WhisperScreen.Chat -> ChatScreen()
                        WhisperScreen.Projects -> ProjectScreen()
                        WhisperScreen.Code -> CodeScreen()
                    }
                }
            }
        )
    }
}

// Écrans principaux de l'application
enum class WhisperScreen(val title: String, val icon: ImageVector) {
    Chat("Chat", Icons.Default.Chat),
    Projects("Projets", Icons.Default.FolderOpen),
    Code("Code", Icons.Default.Code)
}

// Écrans de base à implémenter
@Composable
fun ChatScreen() {
    // TODO: Implémenter l'écran de chat IA
    Box(modifier = Modifier.fillMaxSize()) {
        Text("Écran de Chat IA")
    }
}

@Composable
fun ProjectScreen() {
    // TODO: Implémenter l'écran de gestion de projets
    Box(modifier = Modifier.fillMaxSize()) {
        Text("Écran de Projets")
    }
}

@Composable
fun CodeScreen() {
    // TODO: Implémenter l'écran d'édition de code
    Box(modifier = Modifier.fillMaxSize()) {
        Text("Écran d'Édition de Code")
    }
}