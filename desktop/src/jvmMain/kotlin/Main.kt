import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.delay
import java.io.BufferedReader
import java.io.InputStreamReader

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication, 
        title = "WhisperIDE"
    ) {
        MaterialTheme {
            LoadingScreen()
        }
    }
}

@Composable
fun LoadingScreen() {
    var progress by remember { mutableStateOf(0f) }
    var status by remember { mutableStateOf("Initialisation...") }

    LaunchedEffect(Unit) {
        // Simuler le processus de build Gradle
        val processBuilder = ProcessBuilder("gradle", "desktop:run", "--info")
        processBuilder.redirectErrorStream(true)
        val process = processBuilder.start()

        val reader = BufferedReader(InputStreamReader(process.inputStream))
        var line: String?

        while (reader.readLine().also { line = it } != null) {
            line?.let { 
                when {
                    it.contains("Downloading https") -> {
                        status = "Téléchargement des dépendances..."
                        progress = 0.2f
                    }
                    it.contains("BUILD SUCCESSFUL") -> {
                        status = "Construction terminée"
                        progress = 1f
                    }
                    it.contains("EXECUTING") -> {
                        status = "Compilation en cours..."
                        progress = 0.5f
                    }
                }
                println(it) // Log des messages Gradle
            }
        }

        process.waitFor()
    }

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
                progress = progress,
                color = Color(0xFF3498DB),
                backgroundColor = Color(0xFF34495E),
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
                text = status,
                color = Color(0xFF95A5A6),
                fontSize = 16.sp
            )
        }
    }
}