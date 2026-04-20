package com.example.todoapp.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.todoapp.ui.theme.RedBrand

@Composable
fun AlertDialogPopUp(
    title: String,
    text: String,
    onConfirm: () -> Unit,
    confirmButtonText: String,
    onDismiss: () -> Unit,
    dismissButtonText: String
) {
    AlertDialog(
        onDismissRequest = {},
        title = {Text(text = title, fontWeight = FontWeight.Bold)},
        text = {Text(text)},
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text(text = confirmButtonText, color = RedBrand)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(text = dismissButtonText, color = Color.DarkGray)
            }
        },
        icon = { Icon(imageVector = Icons.Default.Info, contentDescription = null) }
    )
}

@Composable
@Preview
fun AlertDialogPopUpPreview() {
    AlertDialogPopUp(
        title = "Title text",
        text = "Body text",
        onConfirm = {println("User confirmed")},
        onDismiss = {println("User dismissed")},
        confirmButtonText = "Confirm",
        dismissButtonText = "Dismiss"
    )
}