package ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Sync
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import java.io.File

class GitSyncManager(private val projectPath: String, private val githubToken: String, private val repoUrl: String) {
    private val git: Git = Git.open(File(projectPath))

    suspend fun syncWithRemote() = withContext(Dispatchers.IO) {
        try {
            // Pull changes from remote
            git.pull()
                .setCredentialsProvider(UsernamePasswordCredentialsProvider(githubToken, ""))
                .call()

            // Add all changes
            git.add()
                .addFilepattern(".")
                .call()

            // Commit changes
            git.commit()
                .setMessage("Synchronization from WhisperIDE")
                .call()

            // Push to remote
            git.push()
                .setCredentialsProvider(UsernamePasswordCredentialsProvider(githubToken, ""))
                .call()

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}

@Composable
fun FileExplorer(
    rootPath: File,
    githubSyncManager: GitSyncManager? = null,
    onFileSelected: (File) -> Unit,
    modifier: Modifier = Modifier
) {
    var expandedPaths by remember { mutableStateOf(setOf<String>()) }
    var isSyncing by remember { mutableStateOf(false) }
    
    Column(
        modifier = modifier
            .background(Color(0xFF2C3E50))
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Barre d'outils du file explorer avec alignement amélioré
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
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            
            // Bouton de rafraîchissement
            IconButton(
                onClick = { /* Logique de rafraîchissement */ },
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Icon(
                    Icons.Default.Refresh, 
                    contentDescription = "Rafraîchir",
                    tint = Color.White
                )
            }
            
            // Bouton de synchronisation GitHub
            if (githubSyncManager != null) {
                IconButton(
                    onClick = {
                        isSyncing = true
                        // Lancer la synchronisation de manière asynchrone
                        kotlinx.coroutines.GlobalScope.launch {
                            val success = githubSyncManager.syncWithRemote()
                            isSyncing = false
                            // Gérer le résultat (afficher un message, etc.)
                        }
                    },
                    enabled = !isSyncing
                ) {
                    if (isSyncing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            Icons.Default.Sync, 
                            contentDescription = "Synchroniser avec GitHub",
                            tint = Color.White
                        )
                    }
                }
            }
        }
        
        // Liste des fichiers avec scrolling amélioré
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF34495E))
                .verticalScroll(rememberScrollState())
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
            // Icône dossier/fichier avec meilleur alignement
            Text(
                if (file.isDirectory) "📁" else "📄",
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .width(24.dp)
            )
            
            // Nom du fichier avec overflow amélioré
            Text(
                file.name,
                color = Color.White,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
        }
        
        // Afficher le contenu du dossier si expandé
        if (file.isDirectory && file.absolutePath in expandedPaths) {
            file.listFiles()\
                ?.sortedWith(compareBy({ !it.isDirectory }, { it.name }))\
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
