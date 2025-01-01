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
import com.whisperide.shared.project.ProjectManager
import github.GithubAuth
import github.GithubManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ui.components.*
import java.io.File

// Instances globales
private val projectManager = ProjectManager(File(System.getProperty("user.home"), "WhisperIDE/Projects"))
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
    var showProjectManager by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            delay(2000)
            isLoading = false
            // Ouvrir automatiquement le gestionnaire de projets si aucun projet n'est ouvert
            if (projectManager.currentProject == null) {
                showProjectManager = true
            }
        }
    }

    if (isLoading) {
        LoadingScreen()
    } else {
        MainScreen(showProjectManager) { showProjectManager = false }
    }
    
    // Afficher la boîte de dialogue du gestionnaire de projets si nécessaire
    if (showProjectManager) {
        ProjectDialog(
            projectManager = projectManager,
            onDismiss = { showProjectManager = false }
        )
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
fun SidebarIcon(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(60.dp),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(48.dp)
        ) {
            Text(
                text = text,
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}