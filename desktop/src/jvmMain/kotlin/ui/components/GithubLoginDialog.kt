package ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import github.githubAuth
import github.githubManager
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GitHubLoginDialog(
    onDismissRequest: () -> Unit,
    onTokenValidated: () -> Unit
) {
    var token by remember { mutableStateOf("") }
    var expirationDate by remember { mutableStateOf(LocalDate.now().plusDays(30)) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showErrorMessage by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text("Configuration GitHub")
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (showErrorMessage) {
                    Text(
                        errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                OutlinedTextField(
                    value = token,
                    onValueChange = { 
                        token = it
                        showErrorMessage = false
                    },
                    label = { Text("Token GitHub") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = expirationDate.toString(),
                    onValueChange = { },
                    label = { Text("Date d'expiration") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Default.DateRange, "Sélectionner une date")
                        }
                    }
                )
                
                if (showDatePicker) {
                    DatePickerDialog(
                        onDismissRequest = { showDatePicker = false },
                        confirmButton = {
                            TextButton(onClick = { showDatePicker = false }) {
                                Text("OK")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDatePicker = false }) {
                                Text("Annuler")
                            }
                        }
                    ) {
                        DatePicker(
                            state = rememberDatePickerState(
                                initialSelectedDateMillis = expirationDate
                                    .atStartOfDay()
                                    .toInstant(java.time.ZoneOffset.UTC)
                                    .toEpochMilli()
                            ),
                            showModeToggle = false,
                            title = { Text("Date d'expiration") }
                        )
                    }
                }

                if (githubAuth.isAuthenticated()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        githubAuth.getRemainingDays()?.let { days ->
                            Text(
                                "Token valide pendant encore $days jours",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (days < 7) MaterialTheme.colorScheme.error
                                       else MaterialTheme.colorScheme.onSurface
                            )
                        }
                        TextButton(
                            onClick = {
                                githubManager.disconnectGithub()
                                token = ""
                            }
                        ) {
                            Text("Déconnexion")
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    when {
                        token.isBlank() -> {
                            errorMessage = "Le token ne peut pas être vide"
                            showErrorMessage = true
                            return@Button
                        }
                        expirationDate.isBefore(LocalDate.now()) -> {
                            errorMessage = "La date d'expiration doit être future"
                            showErrorMessage = true
                            return@Button
                        }
                    }

                    if (githubManager.initializeGithub(token, expirationDate)) {
                        onTokenValidated()
                        onDismissRequest()
                    } else {
                        errorMessage = "Erreur lors de la validation du token"
                        showErrorMessage = true
                    }
                }
            ) {
                Text("Valider")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Annuler")
            }
        }
    )
}