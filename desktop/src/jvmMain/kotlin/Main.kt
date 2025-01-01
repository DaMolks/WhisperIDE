import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ui.layout.WhisperMainLayout
import ui.theme.WhisperTheme

@Composable
@Preview
fun App() {
    WhisperTheme {
        WhisperMainLayout()
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "WhisperIDE") {
        App()
    }
}