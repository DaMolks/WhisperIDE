package ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import github.GithubAuth
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@Composable
fun GithubLoginDialog(
    auth: GithubAuth,
    onDismiss: () -> Unit
) {
    var token by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Connexion GitHub") },
        text = {
            Column {
                Text("Entrez votre token d'accès GitHub :")
                Spacer(Modifier.height(8.dp))
                TextField(
                    value = token,
                    onValueChange = { 
                        token = it
                        isError = false
                        errorMessage = ""
                    },
                    isError = isError,
                    label = { Text("Personal Access Token") },
                    singleLine = true,
                    enabled = !isLoading
                )
                if (isError) {
                    Text(
                        errorMessage,
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.caption
                    )
                }
                
                if (isLoading) {
                    Spacer(Modifier.height(8.dp))
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (token.isNotBlank()) {
                        scope.launch {
                            isLoading = true
                            try {
                                val success = auth.updateToken(token)
                                if (success) {
                                    onDismiss()
                                } else {
                                    isError = true
                                    errorMessage = "Token invalide"
                                }
                            } catch (e: Exception) {
                                isError = true
                                errorMessage = e.message ?: "Erreur lors de la validation"
                            } finally {
                                isLoading = false
                            }
                        }
                    } else {
                        isError = true
                        errorMessage = "Token requis"
                    }
                },
                enabled = !isLoading
            ) {
                Text("Se connecter")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isLoading
            ) {
                Text("Annuler")
            }
        }
    )
}
