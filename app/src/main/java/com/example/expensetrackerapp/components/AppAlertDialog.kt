package com.example.expensetrackerapp.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun AppAlertDialog(
    title: String,
    text: String,
    onClickConfirm: () -> Unit,
    onClickDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onClickDismiss,
        title = { Text(text = title) },
        text = { Text(text = text) },
        confirmButton = {
            TextButton(onClick = {
                onClickConfirm()
                onClickDismiss()
            }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onClickDismiss) {
                Text("Cancel")
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    )
}