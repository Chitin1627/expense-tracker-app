package com.example.expensetrackerapp.components

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun CreateExpensePopup(
    onSave: (amount: Double, category: String, description: String, date: String) -> Unit,
    onDismiss: () -> Unit
) {
    var amount by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf("") }
    var selectedDate by rememberSaveable { mutableStateOf(System.currentTimeMillis()) }

    // Date Picker
    val context = LocalContext.current
    val dateFormatter = remember { SimpleDateFormat("yyy-MM-dd", Locale.getDefault()) }
    val dateDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }
            selectedDate = calendar.timeInMillis
        },
        Calendar.getInstance().get(Calendar.YEAR),
        Calendar.getInstance().get(Calendar.MONTH),
        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    )

    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column {
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Category") }
            )
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") }
            )
            Spacer(Modifier.height(16.dp))

            Text("Selected Date: ${dateFormatter.format(Date(selectedDate))}")

            Spacer(Modifier.height(8.dp))

            Button(onClick = { dateDialog.show() }) {
                Text("Select Date")
            }
            Spacer(Modifier.height(16.dp))

            Row {
                Button(onClick = onDismiss) {
                    Text("Cancel")
                }
                Spacer(Modifier.width(16.dp))
                Button(onClick = {
                    val amountDouble = amount.toDoubleOrNull()
                    if (amountDouble != null) {
                        val formattedDate = dateFormatter.format(Date(selectedDate))
                        onSave(amountDouble, category, description, formattedDate)
                        amount = ""
                        description = ""
                        category = ""
                        selectedDate = System.currentTimeMillis()
                    }
                }) {
                    Text("Save")
                }
            }
        }
    }
}
