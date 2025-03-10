package com.example.expensetrackerapp.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateDebtComponent(
    onSave: suspend (debtId: String, amount: Double, otherParty: String, description: String, completed: Boolean, receivable: Boolean) -> Boolean,
    onDismiss: () -> Unit,
    debtId: String = "",
    currentAmount: String = "",
    currentOtherParty: String = "",
    currentDescription: String = "",
    currentReceivable: Boolean = false,
    currentCompleted: Boolean = false,
) {
    var amount by rememberSaveable { mutableStateOf(currentAmount) }
    var otherParty by rememberSaveable { mutableStateOf(currentOtherParty) }
    var description by rememberSaveable { mutableStateOf(currentDescription) }
    var receivable by rememberSaveable { mutableStateOf(currentReceivable) }
    var completed by rememberSaveable { mutableStateOf(currentCompleted) }
    var saveButtonEnabled by rememberSaveable { mutableStateOf(false) }
    var isLoading by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }
    var expandedCompleted by rememberSaveable { mutableStateOf(false) }
    var expandedReceivable by rememberSaveable { mutableStateOf(false) }

    saveButtonEnabled = (
            (amount != "") &&
                    !amount.contains("-") &&
                    !(amount.contains(" ")) &&
                    (amount.toDouble() > 0.0)
            )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AutoResizedText(
            text = AnnotatedString("Don't put any personal information"),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(4.dp))

        OutlinedTextField(
            value = amount,
            label = { Text(text = "Amount") },
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Decimal
            ),
            onValueChange = {
                amount = it
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(4.dp))

        OutlinedTextField(
            value = otherParty,
            label = { Text(text = "Other Party") },
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            onValueChange = {
                otherParty = it
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(4.dp))

        // Completed Dropdown
        ExposedDropdownMenuBox(
            expanded = expandedCompleted,
            onExpandedChange = {
                expandedCompleted = !expandedCompleted
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = if (completed) "Yes" else "No",
                label = { Text(text = "Completed?") },
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expandedCompleted,
                onDismissRequest = { expandedCompleted = false },
                modifier = Modifier
                    .padding(top = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                DropdownMenuItem(
                    text = { Text(text = "Yes") },
                    onClick = {
                        completed = true
                        expandedCompleted = false
                    }
                )

                DropdownMenuItem(
                    text = { Text(text = "No") },
                    onClick = {
                        completed = false
                        expandedCompleted = false
                    }
                )
            }
        }

        Spacer(modifier = Modifier.padding(4.dp))


        ExposedDropdownMenuBox(
            expanded = expandedReceivable,
            onExpandedChange = {
                expandedReceivable = !expandedReceivable
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = if (receivable) "Yes" else "No",
                label = { Text(text = "Receivable?") },
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expandedReceivable,
                onDismissRequest = { expandedReceivable = false },
                modifier = Modifier
                    .padding(top = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                DropdownMenuItem(
                    text = { Text(text = "Yes") },
                    onClick = {
                        receivable = true
                        expandedReceivable = false
                    }
                )

                DropdownMenuItem(
                    text = { Text(text = "No") },
                    onClick = {
                        receivable = false
                        expandedReceivable = false
                    }
                )
            }
        }

        Spacer(modifier = Modifier.padding(4.dp))

        OutlinedTextField(
            value = description,
            label = { Text(text = "Description") },
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            onValueChange = {
                description = it
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(4.dp))

        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(8.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            if (!isLoading) {
                Button(
                    enabled = saveButtonEnabled,
                    onClick = {
                        isLoading = true
                        CoroutineScope(Dispatchers.Main).launch {
                            val success = onSave(
                                debtId,
                                amount.toDouble(),
                                otherParty,
                                description,
                                completed,
                                receivable
                            )
                            isLoading = false
                            if (success) {
                                onDismiss()
                            } else {
                                errorMessage = "Failed to save debt. Please try again"
                            }
                        }
                    },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .wrapContentSize(Alignment.Center)
                ) {
                    Text(text = "Save")
                }

                Spacer(modifier = Modifier.padding(4.dp))

                Button(
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text(text = "Cancel")
                }
            } else {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 2.dp
                )
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            amount = ""
            description = ""
            receivable = false
            completed = false
        }
    }
}