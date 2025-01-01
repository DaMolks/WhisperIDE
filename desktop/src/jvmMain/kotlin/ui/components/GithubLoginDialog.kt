package ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import github.GithubAuth
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Composable
fun GithubLoginDialog(
    auth: GithubAuth,
    onDismiss: () -> Unit
) {
    var token by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Connexion GitHub") },
        text = {
            Column {
                // Token GitHub
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
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Date d'expiration
                Spacer(Modifier.height(16.dp))
                Text("Date d'expiration (format: JJ/MM/AAAA) :")
                Spacer(Modifier.height(8.dp))
                TextField(
                    value = expiryDate,
                    onValueChange = { 
                        expiryDate = it
                        isError = false
                        errorMessage = ""
                    },
                    isError = isError,
                    label = { Text("Date d'expiration") },
                    singleLine = true,
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth()
                )
                
                if (isError) {
                    Spacer(Modifier.height(8.dp))
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
                    scope.launch {
                        if (token.isBlank()) {
                            isError = true
                            errorMessage = "Token requis"
                            return@launch
                        }
                        
                        if (expiryDate.isBlank()) {
                            isError = true
                            errorMessage = "Date d'expiration requise"
                            return@launch
                        }
                        
                        // Parser la date
                        val expiry = try {
                            val parts = expiryDate.split("/")
                            if (parts.size != 3) throw Exception("Format de date invalide")
                            val day = parts[0].toInt()
                            val month = parts[1].toInt()
                            val year = parts[2].toInt()
                            LocalDateTime.of(
                                LocalDate.of(year, month, day),
                                LocalTime.MAX
                            )
                        } catch (e: Exception) {
                            isError = true
                            errorMessage = "Format de date invalide (utilisez JJ/MM/AAAA)"
                            return@launch
                        }
                        
                        isLoading = true
                        try {
                            val success = auth.updateToken(token, expiry)
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
