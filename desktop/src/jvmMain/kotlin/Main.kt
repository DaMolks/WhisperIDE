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

    LaunchedEffect(Unit) {
        while (progress < 1f) {
            delay(500) // Simule la progression du chargement
            progress = minOf(progress + 0.1f, 1f)
        }
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
                text = "Initialisation...",
                color = Color(0xFF95A5A6),
                fontSize = 16.sp
            )
        }
    }
}