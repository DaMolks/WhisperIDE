package ui.layout

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import ui.components.*
import ui.theme.WhisperTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhisperMainLayout() {
    var selectedScreen by remember { mutableStateOf(WhisperScreen.Chat) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    WhisperTheme {
        DismissibleNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    Text("WhisperIDE", modifier = Modifier.padding(16.dp))
                    Divider()
                    NavigationDrawerItem(
                        label = { Text("Fichiers") },
                        icon = { Icon(Icons.Default.Folder, contentDescription = "Fichiers") },
                        selected = false,
                        onClick = { /* TODO: Implémenter l'ouverture de l'explorateur de fichiers */ }
                    )
                    NavigationDrawerItem(
                        label = { Text("Paramètres") },
                        icon = { Icon(Icons.Default.Settings, contentDescription = "Paramètres") },
                        selected = false,
                        onClick = { selectedScreen = WhisperScreen.Settings }
                    )
                }
            }
        ) {
            Scaffold(
                topBar = {
                    SmallTopAppBar(
                        title = { Text("WhisperIDE") },
                        navigationIcon = {
                            IconButton(onClick = { 
                                scope.launch {
                                    if (drawerState.isClosed) drawerState.open() else drawerState.close()
                                }
                            }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        },
                        actions = {
                            IconButton(onClick = { selectedScreen = WhisperScreen.Settings }) {
                                Icon(Icons.Default.Settings, contentDescription = "Paramètres")
                            }
                        }
                    )
                },
                bottomBar = {
                    NavigationBar {
                        WhisperScreen.values()
                            .filterNot { it == WhisperScreen.Settings }
                            .forEach { screen ->
                                NavigationBarItem(
                                    icon = { Icon(screen.icon, contentDescription = screen.title) },
                                    label = { Text(screen.title) },
                                    selected = selectedScreen == screen,
                                    onClick = { selectedScreen = screen }
                                )
                            }
                    }
                }
            ) { padding ->
                Box(modifier = Modifier.padding(padding)) {
                    when (selectedScreen) {
                        WhisperScreen.Chat -> ChatInterface()
                        WhisperScreen.Projects -> ProjectManagementScreen()
                        WhisperScreen.Code -> CodeEditorScreen()
                        WhisperScreen.Settings -> SettingsScreen()
                    }
                }
            }
        }
    }
}

// Écrans principaux de l'application
enum class WhisperScreen(val title: String, val icon: ImageVector) {
    Chat("Chat", Icons.Default.Chat),
    Projects("Projets", Icons.Default.FolderOpen),
    Code("Code", Icons.Default.Code),
    Settings("Paramètres", Icons.Default.Settings)
}