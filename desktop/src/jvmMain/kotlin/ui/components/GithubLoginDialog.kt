package ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import github.GithubAuth

@Composable
fun GithubLoginDialog(
    auth: GithubAuth,
    onDismiss: () -> Unit
) {
    var token by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    
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
                    },
                    isError = isError,
                    label = { Text("Personal Access Token") },
                    singleLine = true
                )
                if (isError) {
                    Text(
                        "Token invalide",
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.caption
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                if (token.isNotBlank()) {
                    auth.setToken(token)
                    onDismiss()
                } else {
                    isError = true
                }
            }) {
                Text("Se connecter")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annuler")
            }
        }
    )
}
