import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import github.GithubAuth
import github.GithubManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ui.components.*
import java.time.LocalDateTime

// Instances globales pour la gestion de GitHub
private val githubAuth = GithubAuth()
private val githubManager = GithubManager(githubAuth)

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "WhisperIDE"
    ) {
        MaterialTheme {
            App()
        }
    }
}

@Composable
fun App() {
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            delay(2000)
            isLoading = false
        }
    }

    if (isLoading) {
        LoadingScreen()
    } else {
        MainScreen()
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2C3E50)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(10.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "WhisperIDE",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Chargement...",
                color = Color(0xFF95A5A6),
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun MainScreen() {
    var showSettings by remember { mutableStateOf(false) }
    var showGithubLogin by remember { mutableStateOf(false) }
    var showLogoutConfirm by remember { mutableStateOf(false) }
    
    Box(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxSize()) {
            // Barre latérale gauche
            Column(
                modifier = Modifier
                    .width(60.dp)
                    .fillMaxHeight()
                    .background(Color(0xFF2C3E50))
            ) {
                // Bouton paramètres
                IconButton(onClick = { showSettings = true }) {
                    Text("⚙️", fontSize = 24.sp)
                }
                
                // Bouton GitHub
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(60.dp)
                ) {
                    IconButton(
                        onClick = { 
                            if (githubAuth.isAuthenticated()) {
                                showLogoutConfirm = true
                            } else {
                                showGithubLogin = true
                            }
                        }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            if (githubAuth.isAuthenticated()) {
                                Text("🔓", fontSize = 24.sp)
                            } else {
                                Text("🔒", fontSize = 24.sp)
                            }
                        }
                    }
                    
                    // Statut et date d'expiration
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

            // Zone principale
            Column(modifier = Modifier.weight(1f)) {
                TopAppBar(
                    title = { Text("WhisperIDE") },
                    backgroundColor = Color(0xFF34495E)
                )

                Row(modifier = Modifier.weight(1f)) {
                    // Zone de code
                    Column(modifier = Modifier.weight(0.7f)) {
                        Surface(
                            color = Color(0xFF2C3E50),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                "Zone de code",
                                color = Color.White,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }

                    // Zone de chat
                    Column(modifier = Modifier.weight(0.3f)) {
                        Surface(
                            color = Color(0xFF34495E),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                "Zone de chat",
                                color = Color.White,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
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
