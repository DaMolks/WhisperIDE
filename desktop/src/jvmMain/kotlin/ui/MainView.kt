package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.ProjectManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MainView() {
    var initializationStatus by remember { mutableStateOf("Démarrage de WhisperIDE...") }
    var initializationProgress by remember { mutableStateOf(0f) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val steps = listOf(
                "Préparation de l'environnement",
                "Chargement des configurations",
                "Initialisation des plugins",
                "Connexion aux services",
                "WhisperIDE est prêt !"
            )

            steps.forEachIndexed { index, step ->
                initializationStatus = step
                initializationProgress = (index + 1).toFloat() / steps.size
                delay(500) // Simule le temps de chargement
            }
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
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "WhisperIDE",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = initializationProgress,
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(8.dp),
                backgroundColor = Color(0xFF34495E),
                color = Color(0xFF3498DB)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = initializationStatus,
                color = Color(0xFFBDC3C7),
                fontSize = 16.sp
            )
        }
    }
}