package ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SidebarIcon(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(60.dp),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(48.dp)
        ) {
            Text(
                text = text,
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}