package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodeEditorScreen() {
    var currentFile by remember { mutableStateOf<String?>(null) }
    var code by remember { mutableStateOf(TextFieldValue("")) }
    var language by remember { mutableStateOf("Kotlin") }

    val supportedLanguages = listOf("Kotlin", "Java", "Python", "JavaScript", "Markdown")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // En-tête de l'éditeur
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Sélection du langage
            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.width(150.dp)
            ) {
                TextField(
                    value = language,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Langage") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.textFieldColors()
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    supportedLanguages.forEach { lang ->
                        DropdownMenuItem(
                            text = { Text(lang) },
                            onClick = { 
                                language = lang
                                expanded = false
                            }
                        )
                    }
                }
            }

            // Boutons d'action
            Row {
                IconButton(onClick = { /* Enregistrer */ }) {
                    Icon(Icons.Default.Save, contentDescription = "Enregistrer")
                }
                IconButton(onClick = { /* Nouveau fichier */ }) {
                    Icon(Icons.Default.AddCircle, contentDescription = "Nouveau")
                }
                IconButton(onClick = { /* Ouvrir fichier */ }) {
                    Icon(Icons.Default.Folder, contentDescription = "Ouvrir")
                }
            }
        }

        // Zone d'édition de code
        TextField(
            value = code,
            onValueChange = { code = it },
            modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray.copy(alpha = 0.1f))
                .padding(top = 16.dp),
            placeholder = { Text("Écrivez votre code ici...") },
            minLines = 20,
            maxLines = Int.MAX_VALUE
        )
    }
}