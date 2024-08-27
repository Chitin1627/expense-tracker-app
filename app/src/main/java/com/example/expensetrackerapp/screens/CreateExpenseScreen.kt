package com.example.expensetrackerapp.screens

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.expensetrackerapp.components.DateSelectorButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.DateTimeException
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateExpenseScreen(
    onSave: suspend (amount: Double, category: String, description: String, date: String) -> Boolean,
    onDismiss: () -> Unit,
    categoryNameMap: HashMap<String, String>
) {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = dateFormat.format(Date())

    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(date) }
    var expanded by remember { mutableStateOf(false) }
    var saveButtonEnabled by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    saveButtonEnabled = (amount != "" && !amount.contains("-") && categoryNameMap[category] != null)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
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
            }
        )

        Spacer(modifier = Modifier.padding(8.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            OutlinedTextField(
                value = category,
                label = { Text(text = "Category") },
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .padding(top = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                categoryNameMap.keys.forEach { categoryItem ->
                    DropdownMenuItem(
                        text = { Text(text = categoryItem) },
                        onClick = {
                            category = categoryItem
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.padding(8.dp))

        OutlinedTextField(
            value = description,
            label = { Text(text = "Description") },
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            onValueChange = {
                description = it
            }
        )

        Spacer(modifier = Modifier.padding(8.dp))

        DateSelectorButton(
            onDateSelected = { date ->
                selectedDate = date
            },
            text = "Select Date"
        )
        Spacer(modifier = Modifier.padding(8.dp))

        Text(text = "Selected Date: $selectedDate")

        Spacer(modifier = Modifier.padding(8.dp))

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
                                amount.toDouble(),
                                categoryNameMap[category] ?: "",
                                description,
                                selectedDate
                            )
                            isLoading = false
                            if (success) {
                                onDismiss()
                            }
                            else {
                                errorMessage = "Failed to save expense. Please try again"
                            }
                        }
                    },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .wrapContentSize(Alignment.Center) // Wraps content size around its content
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
            category = ""
            selectedDate = date
        }
    }
}
