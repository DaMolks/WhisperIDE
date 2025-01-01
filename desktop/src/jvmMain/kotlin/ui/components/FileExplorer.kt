package ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.File

@Composable
fun FileExplorer(
    rootPath: File,
    onFileSelected: (File) -> Unit,
    modifier: Modifier = Modifier
) {
    var expandedPaths by remember { mutableStateOf(setOf<String>()) }
    
    Column(modifier = modifier.background(Color(0xFF2C3E50))) {
        // Barre d'outils du file explorer
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Fichiers",
                color = Color.White,
                fontSize = 14.sp
            )
            
            IconButton(onClick = { /* TODO: Refresh */ }) {
                Text("🔄", fontSize = 16.sp)
            }
        }
        
        // Liste des fichiers
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF34495E))
        ) {
            FileTree(
                file = rootPath,
                level = 0,
                expandedPaths = expandedPaths,
                onExpandToggle = { path ->
                    expandedPaths = if (path in expandedPaths) {
                        expandedPaths - path
                    } else {
                        expandedPaths + path
                    }
                },
                onFileSelected = onFileSelected
            )
        }
    }
}

@Composable
private fun FileTree(
    file: File,
    level: Int,
    expandedPaths: Set<String>,
    onExpandToggle: (String) -> Unit,
    onFileSelected: (File) -> Unit
) {
    val padding = (level * 16).dp
    
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    if (file.isDirectory) {
                        onExpandToggle(file.absolutePath)
                    } else {
                        onFileSelected(file)
                    }
                }
                .padding(start = padding, end = 8.dp, top = 4.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icône dossier/fichier
            Text(
                if (file.isDirectory) "📁" else "📄",
                fontSize = 14.sp,
                modifier = Modifier.padding(end = 4.dp)
            )
            
            // Nom du fichier
            Text(
                file.name,
                color = Color.White,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        
        // Afficher le contenu du dossier si expandé
        if (file.isDirectory && file.absolutePath in expandedPaths) {
            file.listFiles()
                ?.sortedWith(compareBy({ !it.isDirectory }, { it.name }))
                ?.forEach { child ->
                    FileTree(
                        file = child,
                        level = level + 1,
                        expandedPaths = expandedPaths,
                        onExpandToggle = onExpandToggle,
                        onFileSelected = onFileSelected
                    )
                }
        }
    }
}
