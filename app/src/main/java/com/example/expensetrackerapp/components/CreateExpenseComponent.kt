package com.example.expensetrackerapp.components

import androidx.compose.foundation.layout.Arrangement
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
fun CreateExpenseComponent(
    onSave: suspend (expenseId: String, amount: Double, category: String, type: String, description: String, date: String, created_at: String) -> Boolean,
    onDismiss: () -> Unit,
    categoryNameMap: HashMap<String, String>,
    expenseId: String = "",
    currentAmount: String = "",
    currentDescription: String = "",
    currentCategory: String = "",
    currentDate: String = "",
    currentType: String = "",
    created_at: String = ""
) {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    var date = dateFormat.format(Date())
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    if(currentDate!="") date = currentDate

    var amount by rememberSaveable { mutableStateOf(currentAmount) }
    var description by rememberSaveable { mutableStateOf(currentDescription) }
    var category by rememberSaveable { mutableStateOf(currentCategory) }
    var type by rememberSaveable { mutableStateOf(currentType) }
    var selectedDate by rememberSaveable { mutableStateOf(date) }
    var expandedCategory by rememberSaveable { mutableStateOf(false) }
    var expandedType by rememberSaveable { mutableStateOf(false) }
    var saveButtonEnabled by rememberSaveable { mutableStateOf(false) }
    var isLoading by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }

    saveButtonEnabled = ((
            (amount != "") &&
                    !amount.contains("-") &&
                    !(amount.contains(" ")) &&
                    (amount.toDouble() > 0.0) &&
                    (categoryNameMap[category] != null) &&
                    (type != "")
            ))

    AutoResizedText(
        text = AnnotatedString("Don't put any personal information"),
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.padding(8.dp))

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
        expanded = expandedCategory,
        onExpandedChange = {
            expandedCategory = !expandedCategory
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
            expanded = expandedCategory,
            onDismissRequest = { expandedCategory = false },
            modifier = Modifier
                .padding(top = 4.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            categoryNameMap.keys.sorted().forEach { categoryItem ->
                DropdownMenuItem(
                    text = { Text(text = categoryItem) },
                    onClick = {
                        category = categoryItem
                        expandedCategory = false
                    }
                )
            }
        }
    }

    Spacer(modifier = Modifier.padding(8.dp))

    ExposedDropdownMenuBox(
        expanded = expandedType,
        onExpandedChange = {
            expandedType = !expandedType
        }
    ) {
        OutlinedTextField(
            value = type,
            label = { Text(text = "Type") },
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
            expanded = expandedType,
            onDismissRequest = { expandedType = false },
            modifier = Modifier
                .padding(top = 4.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            DropdownMenuItem(
                text = { Text(text = "DEBIT") },
                onClick = {
                    type = "DEBIT"
                    expandedType = false
                }
            )

            DropdownMenuItem(
                text = { Text(text = "CREDIT") },
                onClick = {
                    type = "CREDIT"
                    expandedType = false
                }
            )
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
        onDateSelected = { newDate ->
            selectedDate = newDate
        },
        text = "Select Date"
    )
    Spacer(modifier = Modifier.padding(8.dp))

    Text(
        text = "Selected Date: ${inputFormat.parse(selectedDate)
            ?.let { outputFormat.format(it) }}",
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold
    )

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
                            expenseId,
                            amount.toDouble(),
                            categoryNameMap[category] ?: "",
                            type,
                            description,
                            selectedDate,
                            created_at
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


    DisposableEffect(Unit) {
        onDispose {
            amount = ""
            description = ""
            category = ""
            type = ""
            selectedDate = date
        }
    }
}