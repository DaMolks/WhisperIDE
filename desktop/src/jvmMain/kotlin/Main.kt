import androidx.compose.desktop.ui.tooling.preview.Preview
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
import com.whisperide.shared.project.projectManager
import github.GithubAuth
import github.GithubManager
import github.githubAuth
import github.githubManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ui.components.*
import java.io.File

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
    
    if (showProjectManager) {
        ProjectDialog(
            projectManager = projectManager,
            onDismiss = { showProjectManager = false }
        )
    }
}
