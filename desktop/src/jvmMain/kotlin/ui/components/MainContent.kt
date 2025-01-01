package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.whisperide.shared.project.projectManager

@Composable
fun MainContent() {
    Row(modifier = Modifier.fillMaxSize()) {
        // Explorateur de fichiers
        Box(
            modifier = Modifier
                .width(250.dp)
                .fillMaxHeight()
        ) {
            projectManager.currentProject?.let { project ->
                FileExplorer(
                    rootPath = project.pathFile,
                    onFileSelected = { /* TODO */ },
                    modifier = Modifier.fillMaxSize()
                )
            } ?: run {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF2C3E50)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Aucun projet ouvert\nCliquez sur 📂 pour commencer",
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }

        // Zone principale
        Column(modifier = Modifier.weight(1f)) {
            TopAppBar(
                title = {
                    Text(projectManager.currentProject?.name ?: "WhisperIDE")
                },
                backgroundColor = Color(0xFF34495E)
            )

            Row(modifier = Modifier.weight(1f)) {
                // Zone de code
                Column(modifier = Modifier.weight(0.7f)) {
                    Surface(
                        color = Color(0xFF2C3E50),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Box(
                            modifier = Modifier.padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (projectManager.currentProject == null) {
                                Text(
                                    "Ouvrez un projet pour commencer",
                                    color = Color.Gray
                                )
                            } else {
                                Text(
                                    "TODO: Zone de code",
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                // Zone de chat
                Column(modifier = Modifier.weight(0.3f)) {
                    Surface(
                        color = Color(0xFF34495E),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Box(
                            modifier = Modifier.padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "TODO: Zone de chat",
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}