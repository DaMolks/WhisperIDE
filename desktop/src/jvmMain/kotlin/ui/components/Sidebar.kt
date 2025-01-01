package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import github.GithubAuth

@Composable
fun Sidebar(
    showProjectManager: () -> Unit,
    showSettings: () -> Unit,
    showGithubLogin: () -> Unit,
    showLogoutConfirm: () -> Unit,
    auth: GithubAuth = githubAuth  // Injection de dépendance pour les tests
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
                text = if (auth.isAuthenticated()) "🔓" else "🔒",
                onClick = { 
                    if (auth.isAuthenticated()) {
                        showLogoutConfirm()
                    } else {
                        showGithubLogin()
                    }
                }
            )
            
            if (auth.isAuthenticated()) {
                Text(
                    "Connecté",
                    fontSize = 8.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                
                auth.expirationDate?.let { expDate ->
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