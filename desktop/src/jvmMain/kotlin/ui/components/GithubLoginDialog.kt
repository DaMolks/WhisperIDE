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
import kotlinx.coroutines.launch

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
    var isValidating by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

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
                    singleLine = true,
                    enabled = !isValidating
                )

                OutlinedTextField(
                    value = expirationDate.toString(),
                    onValueChange = { },
                    label = { Text("Date d'expiration") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    enabled = !isValidating,
                    trailingIcon = {
                        IconButton(
                            onClick = { showDatePicker = true },
                            enabled = !isValidating
                        ) {
                            Icon(Icons.Default.DateRange, "Sélectionner une date")
                        }
                    }
                )

                if (isValidating) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                if (githubAuth.isAuthenticated()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Token valide pendant encore ${githubAuth.getRemainingDays()} jours",
                            style = MaterialTheme.typography.bodySmall
                        )
                        TextButton(
                            onClick = {
                                githubManager.disconnectGithub()
                                token = ""
                            },
                            enabled = !isValidating
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

                    scope.launch {
                        isValidating = true
                        showErrorMessage = false
                        try {
                            if (githubManager.initializeGithub(token, expirationDate)) {
                                onTokenValidated()
                                onDismissRequest()
                            } else {
                                errorMessage = "Token GitHub invalide"
                                showErrorMessage = true
                            }
                        } catch (e: Exception) {
                            errorMessage = "Erreur : ${e.message}"
                            showErrorMessage = true
                        } finally {
                            isValidating = false
                        }
                    }
                },
                enabled = !isValidating
            ) {
                Text("Valider")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest,
                enabled = !isValidating
            ) {
                Text("Annuler")
            }
        }
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = { 
                        datePickerState.selectedDateMillis?.let { millis ->
                            expirationDate = java.time.Instant
                                .ofEpochMilli(millis)
                                .atZone(java.time.ZoneId.systemDefault())
                                .toLocalDate()
                        }
                        showDatePicker = false 
                    }
                ) {
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
                        .atZone(java.time.ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli()
                ),
                showModeToggle = false,
                title = { Text("Date d'expiration") }
            )
        }
    }
}