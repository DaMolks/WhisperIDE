import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ui.MainView

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "WhisperIDE") {
        MainView()
    }
}