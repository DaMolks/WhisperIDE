package ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import github.githubManager
import ui.screens.GitHubLoginScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var showGithubLogin by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showGithubLogin) {
        GitHubLoginScreen(
            onBack = { showGithubLogin = false },
            onTokenValidated = { showGithubLogin = false }
        )
    }

    // Dialog de confirmation de déconnexion
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Confirmation") },
            text = { Text("Voulez-vous vraiment vous déconnecter ?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        githubManager.disconnectGithub()
                        showLogoutDialog = false
                    }
                ) {
                    Text("Déconnexion")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Annuler")
                }
            }
        )
    }
}