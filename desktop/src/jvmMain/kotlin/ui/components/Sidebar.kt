package ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import github.githubAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Sidebar(
    showProjectManager: () -> Unit,
    showSettings: () -> Unit,
    showGithubLogin: () -> Unit,
    showLogoutConfirm: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(60.dp)
            .fillMaxHeight()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Icône GitHub et statut
        IconButton(
            onClick = {
                if (githubAuth.isAuthenticated()) {
                    showLogoutConfirm()
                } else {
                    showGithubLogin()
                }
            }
        ) {
            Icon(
                if (githubAuth.isAuthenticated()) Icons.Default.Key
                else Icons.Default.LockOpen,
                contentDescription = "GitHub Token",
                tint = if (githubAuth.isAuthenticated()) MaterialTheme.colorScheme.primary
                       else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (githubAuth.isAuthenticated()) {
            Text(
                "Connecté",
                fontSize = 8.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            githubAuth.getRemainingDays()?.let { days ->
                Text(
                    "$days jours",
                    fontSize = 8.sp,
                    color = if (days < 7) MaterialTheme.colorScheme.error
                           else MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Autres icônes de navigation
        IconButton(onClick = { /* TODO: Nouveau Projet */ }) {
            Icon(Icons.Default.CreateNewFolder, "Nouveau Projet")
        }

        IconButton(onClick = showSettings) {
            Icon(Icons.Default.Settings, "Paramètres")
        }
    }
}