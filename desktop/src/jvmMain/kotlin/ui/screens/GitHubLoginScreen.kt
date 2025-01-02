package ui.screens

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
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GitHubLoginScreen(
    onBack: () -> Unit,
    onTokenValidated: () -> Unit
) {
    var token by remember { mutableStateOf("") }
    var expirationDate by remember { mutableStateOf(LocalDate.now().plusDays(30)) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showErrorMessage by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isValidating by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    
    // Formateur de date pour l'affichage en format européen
    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd/MM/yyyy") }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Configuration GitHub") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                }
            }
        )

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
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
                        value = expirationDate.format(dateFormatter),
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

                    Box(modifier = Modifier.align(Alignment.End)) {
                        if (isValidating) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        } else {
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

                                    coroutineScope.launch {
                                        isValidating = true
                                        try {
                                            if (githubManager.initializeGithub(token, expirationDate)) {
                                                onTokenValidated()
                                            } else {
                                                errorMessage = "Token GitHub invalide"
                                                showErrorMessage = true
                                            }
                                        } catch (e: Exception) {
                                            errorMessage = "Erreur lors de la validation : ${e.message}"
                                            showErrorMessage = true
                                        } finally {
                                            isValidating = false
                                        }
                                    }
                                }
                            ) {
                                Text("Valider")
                            }
                        }
                    }
                }
            }

            if (githubAuth.isAuthenticated()) {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "Statut de la connexion",
                            style = MaterialTheme.typography.titleMedium
                        )
                        
                        githubAuth.getRemainingDays()?.let { days ->
                            Text(
                                "Token valide pendant encore $days jours",
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (days < 7) MaterialTheme.colorScheme.error 
                                       else MaterialTheme.colorScheme.onSurface
                            )
                        }
                        
                        Button(
                            onClick = {
                                githubManager.disconnectGithub()
                                token = ""
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            ),
                            enabled = !isValidating
                        ) {
                            Text("Déconnexion")
                        }
                    }
                }
            }
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = expirationDate
                .atStartOfDay()
                .atZone(java.time.ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        )

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
                state = datePickerState,
                showModeToggle = false,
                title = { Text("Date d'expiration") }
            )
        }
    }
}