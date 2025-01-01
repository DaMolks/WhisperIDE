package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

// Message data class
data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

@Composable
fun ChatInterface() {
    var messages by remember { mutableStateOf(listOf<ChatMessage>()) }
    var inputText by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        // Message List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.DarkGray.copy(alpha = 0.1f), shape = RoundedCornerShape(8.dp)),
            verticalArrangement = Arrangement.Bottom
        ) {
            items(messages) { message ->
                ChatMessageItem(message)
            }
        }

        // Input Area
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Posez votre question à l'IA...") },
                singleLine = false,
                maxLines = 3
            )

            IconButton(
                onClick = {
                    if (inputText.text.isNotBlank()) {
                        // Ajouter le message de l'utilisateur
                        messages += ChatMessage(inputText.text, isUser = true)
                        
                        // TODO: Implémenter la logique de réponse de l'IA
                        messages += ChatMessage("Réponse de l'IA en cours de développement", isUser = false)
                        
                        // Réinitialiser le champ de texte
                        inputText = TextFieldValue("")
                    }
                }
            ) {
                Icon(Icons.Default.Send, contentDescription = "Envoyer")
            }
        }
    }
}

@Composable
fun ChatMessageItem(message: ChatMessage) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(if (message.isUser) Color.Blue.copy(alpha = 0.1f) else Color.Gray.copy(alpha = 0.1f))
                .padding(12.dp)
        ) {
            Text(
                text = message.text,
                color = if (message.isUser) Color.White else Color.White.copy(alpha = 0.8f)
            )
        }
    }
}