import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "WhisperIDE") {
        MaterialTheme {
            MainContent()
        }
    }
}

@Composable
fun MainContent() {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("Welcome to WhisperIDE")
    }
}