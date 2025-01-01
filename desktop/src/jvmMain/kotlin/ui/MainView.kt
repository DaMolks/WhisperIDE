package ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import core.ProjectManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MainView() {
    val projectManager = remember { ProjectManager() }
    var projectName by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            // Simule une initialisation
            delay(2000)
            isLoading = false
            println("Initialisation terminée")
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier.size(50.dp)
            )
        }
    } else {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("WhisperIDE", style = MaterialTheme.typography.h4)
            Spacer(modifier = Modifier.height(16.dp))
            
            TextField(
                value = projectName,
                onValueChange = { projectName = it },
                label = { Text("Project Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { 
                    projectManager.createNewProject(projectName, "./projects/$projectName") 
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Create Project")
            }
        }
    }
}