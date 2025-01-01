package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import github.githubAuth

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
            .background(Color(0xFF2C3E50)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Bouton projets
        SidebarIcon(
            text = "📂",
            onClick = showProjectManager
        )
        
        // Bouton paramètres
        SidebarIcon(
            text = "⚙️",
            onClick = showSettings
        )
        
        // Bouton GitHub avec statut
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(60.dp)
        ) {
            SidebarIcon(
                text = if (githubAuth.isAuthenticated()) "🔓" else "🔒",
                onClick = { 
                    if (githubAuth.isAuthenticated()) {
                        showLogoutConfirm()
                    } else {
                        showGithubLogin()
                    }
                }
            )
            
            if (githubAuth.isAuthenticated()) {
                Text(
                    "Connecté",
                    fontSize = 8.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                
                githubAuth.expirationDate?.let { expDate ->
                    ExpirationDisplay(expDate)
                }
            } else {
                Text(
                    "Déconnecté",
                    fontSize = 8.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}