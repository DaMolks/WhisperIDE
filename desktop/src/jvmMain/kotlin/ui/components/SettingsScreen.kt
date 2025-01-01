package ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.theme.WhisperTheme

@Composable
fun SettingsScreen(
    themeState: WhisperTheme.ThemeState = WhisperTheme.defaultThemeState
) {
    var isDarkMode by remember { mutableStateOf(themeState.isDarkMode) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Paramètres", 
            style = MaterialTheme.typography.headlineMedium
        )

        // Section Thème
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Apparence", 
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Mode sombre")
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = { 
                            isDarkMode = it
                            themeState.toggleTheme() 
                        }
                    )
                }
            }
        }

        // Autres sections de paramètres peuvent être ajoutées ici
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Général", 
                    style = MaterialTheme.typography.titleMedium
                )
                // Futurs paramètres généraux
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}