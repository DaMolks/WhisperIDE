package ui.components

import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Composable
fun ExpirationDisplay(expirationDate: LocalDateTime) {
    val daysUntilExpiry = ChronoUnit.DAYS.between(
        LocalDateTime.now(),
        expirationDate
    )
    
    val (text, color) = when {
        daysUntilExpiry <= 0 -> "Expiré" to Color.Red
        daysUntilExpiry <= 5 -> "Expire dans $daysUntilExpiry jours" to Color(0xFFFF7F7F)
        daysUntilExpiry <= 7 -> "Expire dans $daysUntilExpiry jours" to Color(0xFFFFD700)
        daysUntilExpiry <= 15 -> "Expire dans $daysUntilExpiry jours" to Color(0xFFBBFF7F)
        else -> "Expire dans $daysUntilExpiry jours" to Color(0xFF7FFF7F)
    }
    
    Text(
        text = text,
        fontSize = 8.sp,
        color = color,
        textAlign = TextAlign.Center
    )
}
