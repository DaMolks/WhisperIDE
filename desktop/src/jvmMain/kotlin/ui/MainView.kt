package ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import core.ProjectManager

@Composable
fun MainView() {
    val projectManager = remember { ProjectManager() }
    var projectName by remember { mutableStateOf("") }

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