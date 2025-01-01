package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import github.githubAuth

@Composable
fun MainScreen(showProjectManager: Boolean = false, onProjectManagerDismiss: () -> Unit = {}) {
    var showSettings by remember { mutableStateOf(false) }
    var showGithubLogin by remember { mutableStateOf(false) }
    var showLogoutConfirm by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxSize()) {
            // Sidebar
            Sidebar(
                showProjectManager = onProjectManagerDismiss,
                showSettings = { showSettings = true },
                showGithubLogin = { showGithubLogin = true },
                showLogoutConfirm = { showLogoutConfirm = true }
            )

            // Zone principale
            MainContent()
        }

        // Dialogs
        if (showSettings) {
            AlertDialog(
                onDismissRequest = { showSettings = false },
                title = { Text("Paramètres") },
                text = { Text("Configuration de WhisperIDE") },
                confirmButton = {
                    Button(onClick = { showSettings = false }) {
                        Text("Fermer")
                    }
                }
            )
        }
        
        if (showGithubLogin) {
            GithubLoginDialog(
                auth = githubAuth,
                onDismiss = { showGithubLogin = false }
            )
        }
        
        if (showLogoutConfirm) {
            LogoutConfirmation(
                onConfirm = {
                    githubAuth.clearToken()
                    showLogoutConfirm = false
                },
                onDismiss = { showLogoutConfirm = false }
            )
        }
    }
}
