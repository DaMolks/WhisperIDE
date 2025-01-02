package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import github.githubAuth
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GitHubLoginScreen(
    onBack: () -> Unit
) {
    var token by remember { mutableStateOf("") }
    var expirationDate by remember { mutableStateOf(LocalDate.now().plusDays(30)) }
    var showDatePicker by remember { mutableStateOf(false) }
    
    Column(modifier = Modifier.fillMaxSize()) {
        // Barre supérieure
        TopAppBar(
            title = { Text("Configuration GitHub") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                }
            }
        )

        // Contenu
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
                    Text(
                        "Configuration de l'accès GitHub",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    OutlinedTextField(
                        value = token,
                        onValueChange = { token = it },
                        label = { Text("Token GitHub") },
                        modifier = Modifier.fillMaxWidth()
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

                    Button(
                        onClick = {
                            githubAuth.setCredentials(token, expirationDate)
                            onBack()
                        },
                        modifier = Modifier.align(Alignment.End),
                        enabled = token.isNotBlank()
                    ) {
                        Text("Enregistrer")
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
                                githubAuth.clearCredentials()
                                token = ""
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text("Déconnexion")
                        }
                    }
                }
            }
        }
    }
}